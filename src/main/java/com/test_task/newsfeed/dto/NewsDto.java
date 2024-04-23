package com.test_task.newsfeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewsDto {
    String product;

    double price;

    String destination;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("user_login")
    String login;

    LocalDateTime createdAt;
}
