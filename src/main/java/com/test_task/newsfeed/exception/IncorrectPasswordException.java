package com.test_task.newsfeed.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
