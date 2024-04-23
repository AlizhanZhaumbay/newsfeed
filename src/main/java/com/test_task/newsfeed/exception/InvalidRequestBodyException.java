package com.test_task.newsfeed.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class InvalidRequestBodyException extends RuntimeException {
    private Set<String> errorMessages;

}
