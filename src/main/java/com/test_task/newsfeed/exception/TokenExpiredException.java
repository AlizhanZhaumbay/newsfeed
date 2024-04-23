package com.test_task.newsfeed.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenExpiredException extends RuntimeException {

    private String message;
}
