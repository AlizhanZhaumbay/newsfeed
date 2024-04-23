package com.test_task.newsfeed;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.test_task.newsfeed.auth.TokenRepository;
import com.test_task.newsfeed.dto.NewsDtoFactory;
import com.test_task.newsfeed.model.News;
import com.test_task.newsfeed.model.User;
import com.test_task.newsfeed.network.AuthenticationResponse;
import com.test_task.newsfeed.network.NewsCreationRequest;
import com.test_task.newsfeed.network.RegisterRequest;
import com.test_task.newsfeed.repo.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class NewsTests {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final static String BASE_URL = "https://localhost:8080/";

    @Test
    @DirtiesContext
    @Transactional
    @SneakyThrows
    void handleNewsCreation() {
        var url = "news";
        String userAccessToken = handleAuthentication_ReturnsAccessToken();

        NewsCreationRequest newsCreationRequest = new NewsCreationRequest("Ipone 14 Pro Max",
                1499, "Astana/Kazakhstan", "+7 (777) 777-7777"
        );

        var requestBuilder = post(
                BASE_URL + url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", getAuthorizationHeaderValue(userAccessToken))
                .content(objectMapper.writeValueAsString(newsCreationRequest));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @DirtiesContext
    @Transactional
    @SneakyThrows
    void handleNewsFetching() {
        var url = "news";
        String userAccessToken = handleAuthentication_ReturnsAccessToken();
        User user = getUserByAccessToken(userAccessToken);
        News news1 = new News("product1", 1499, "City/Country",
                "+7 (777) 777-7777", user.getLogin());
        News news2 = new News("product2", 1299, "City/Country",
                "+7 (777) 777-7777", user.getLogin());
        News news3 = new News("product3", 1399, "City/Country",
                "+7 (777) 777-7777", user.getLogin());

        newsRepository.saveAll(List.of(news1, news2, news3));

        var requestBuilder = get(BASE_URL + url)
                .header("Authorization", getAuthorizationHeaderValue(userAccessToken));

        registerJavaTimeModule();
        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(
                                List.of(NewsDtoFactory.convert(news1),
                                        NewsDtoFactory.convert(news2),
                                        NewsDtoFactory.convert(news3)
                        )))
                );
    }

    @DirtiesContext
    @Transactional
    @SneakyThrows
    String handleAuthentication_ReturnsAccessToken() {
        var register = "auth/register";

        RegisterRequest registerRequest =
                new RegisterRequest("login", "pasSword1",
                        "pasSword1",
                        null,
                        "name",
                        "surname", null);

        var registerRequestBuilder = post(BASE_URL + register)
                .content(objectMapper.writeValueAsString(registerRequest))
                .contentType(MediaType.APPLICATION_JSON);

        String jsonResponseContent = mockMvc.perform(registerRequestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();

        AuthenticationResponse authenticationResponse =
                objectMapper.readValue(jsonResponseContent, AuthenticationResponse.class);
        return authenticationResponse.getAccessToken();
    }

    private User getUserByAccessToken(String accessToken){
        return tokenRepository.findByToken(accessToken).get().getUser();
    }

    private String getAuthorizationHeaderValue(String accessToken) {
        return String.format("Bearer %s", accessToken);
    }


    private void registerJavaTimeModule(){
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")));
        objectMapper.registerModule(module);
    }
}
