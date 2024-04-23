package com.test_task.newsfeed.service;

import com.test_task.newsfeed.dto.NewsDto;
import com.test_task.newsfeed.dto.NewsDtoFactory;
import com.test_task.newsfeed.exception.NewsNotFoundException;
import com.test_task.newsfeed.model.News;
import com.test_task.newsfeed.model.User;
import com.test_task.newsfeed.network.NewsCreationRequest;
import com.test_task.newsfeed.repo.NewsRepository;
import com.test_task.newsfeed.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final ObjectValidator<NewsCreationRequest> objectValidator;

    private final NewsRepository newsRepository;

    public Long saveNews(NewsCreationRequest newsRequest) {
        objectValidator.validate(newsRequest);
        User currentUser = (User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        News news = newsRepository.save(News.builder()
                        .product(newsRequest.getProduct())
                        .destination(newsRequest.getDestination())
                        .price(newsRequest.getPrice())
                        .phoneNumber(newsRequest.getPhoneNumber())
                        .userLogin(currentUser.getLogin())
                .build());
        return news.getId();
    }

    public List<NewsDto> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .map(NewsDtoFactory::convert)
                .toList();
    }

    public NewsDto getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(NewsDtoFactory::convert)
                .orElseThrow(() -> new NewsNotFoundException(String.format("News not found with id: %d", id)));
    }
}
