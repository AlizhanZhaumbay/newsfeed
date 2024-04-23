package com.test_task.newsfeed.dto;

import com.test_task.newsfeed.model.News;

public class NewsDtoFactory {
    public static NewsDto convert(News news){
        return new NewsDto(news.getProduct(),
                news.getPrice(),
                news.getDestination(),
                news.getPhoneNumber(),
                news.getUserLogin(),
                news.getCreatedAt());
    }
}
