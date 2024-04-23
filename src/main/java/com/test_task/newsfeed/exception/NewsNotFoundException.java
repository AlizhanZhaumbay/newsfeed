package com.test_task.newsfeed.exception;


public class NewsNotFoundException extends RuntimeException {

    public NewsNotFoundException(String message) {
        super(message);
    }
}
