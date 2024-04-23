package com.test_task.newsfeed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_task.newsfeed.network.AuthenticationResponse;
import com.test_task.newsfeed.network.LoginRequest;
import com.test_task.newsfeed.network.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AuthenticationTests {

    @Autowired
    MockMvc mockMvc;

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "https://localhost:8080/auth";

    @Test
    @DirtiesContext
    @Transactional
    @SneakyThrows
    void handleAuthentication_ShouldPassSuccessfully() {
        var login = "/login";
        var register = "/register";

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

        LoginRequest loginRequest = new LoginRequest(
                "login", "pasSword1"
        );
        var loginRequestBuilder = post(BASE_URL + login)
                .content(objectMapper.writeValueAsString(loginRequest))
                .header("Authorization", getAuthorizationHeaderValue(authenticationResponse.getAccessToken()))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(loginRequestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

    }

    @Test
    @DirtiesContext
    @Transactional
    @SneakyThrows
    void handleRegistrationWithIncorrectParameters_ShouldReturnStatusBadGateway() {
        var register = "/register";

        RegisterRequest registerRequest =
                new RegisterRequest("$log", "pasSword1",
                        "passwrd",
                        "asdasdasasd",
                        "nam",
                        "sur", null);

        var registerRequestBuilder = post(BASE_URL + register)
                .content(objectMapper.writeValueAsString(registerRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(registerRequestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    @DirtiesContext
    @Transactional
    @SneakyThrows
    void handleRegistrationWithDifferentPassword_ShouldReturnStatusBadGateway() {
        var register = "/register";

        RegisterRequest registerRequest =
                new RegisterRequest("login", "pasSword1",
                        "passWrd1",
                        null,
                        "name",
                        "surname", null);

        var registerRequestBuilder = post(BASE_URL + register)
                .content(objectMapper.writeValueAsString(registerRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(registerRequestBuilder).andExpectAll(
                status().isBadRequest(),
                content().string("Confirmation password should be the same as password"),
                content().contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
        ).andReturn().getResponse().getContentAsString();
    }

    @Test
    @DirtiesContext
    @Transactional
    @SneakyThrows
    void handleLoginWithIncorrectPassword_ShouldReturnStatusForbidden() {
        var login = "/login";
        var register = "/register";

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

        //Invalid password
        LoginRequest loginRequest = new LoginRequest(
                "login", "pasSSword1"
        );
        var loginRequestBuilder = post(BASE_URL + login)
                .content(objectMapper.writeValueAsString(loginRequest))
                .header("Authorization", getAuthorizationHeaderValue(authenticationResponse.getAccessToken()))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(loginRequestBuilder)
                .andExpectAll(
                        status().isForbidden(),
                        content().string("Invalid login or password"),
                        content().contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                );

    }

    private String getAuthorizationHeaderValue(String accessToken) {
        return String.format("Bearer %s", accessToken);
    }
}
