package com.test_task.newsfeed.network;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequest implements AuthenticationRequest{
        @NotEmpty(message = "Login must not be null or empty.")
        String login;

        @NotEmpty(message = "Password must not be null or empty.")
        String password;


}
