package com.test_task.newsfeed.controller;

import com.test_task.newsfeed.auth.AuthenticationService;
import com.test_task.newsfeed.network.AuthenticationResponse;
import com.test_task.newsfeed.network.LoginRequest;
import com.test_task.newsfeed.network.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "A controller for processing authentication requests")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(description = "Register a new user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not authorized",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Invalid request body",
                            responseCode = "400"
                    )
            })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(description = "Sign in and authenticate user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not authorized",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Invalid request body",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @Operation(description = "Refresh access token",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not authorized",
                            responseCode = "403"
                    ),
            })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
