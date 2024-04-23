package com.test_task.newsfeed.validator;

import com.test_task.newsfeed.exception.IncorrectPasswordException;
import com.test_task.newsfeed.exception.InvalidRequestBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;
import java.util.Set;

@RestControllerAdvice
public class NewsfeedExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid login or password");
    }

    @ExceptionHandler({InvalidRequestBodyException.class})
    public ResponseEntity<Set<String>> handleInvalidBodyException(InvalidRequestBodyException exception){
        return ResponseEntity.badRequest().body(exception.getErrorMessages());
    }

    @ExceptionHandler({IncorrectPasswordException.class})
    public ResponseEntity<String> invalidParameterException(IncorrectPasswordException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
