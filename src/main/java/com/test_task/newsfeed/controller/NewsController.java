package com.test_task.newsfeed.controller;

import com.test_task.newsfeed.dto.NewsDto;
import com.test_task.newsfeed.network.NewsCreationRequest;
import com.test_task.newsfeed.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "News")
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class NewsController {
    private final NewsService newsService;


    @Operation(description = "Endpoint for creating a new piece of news",
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
    @PostMapping
    public ResponseEntity<Long> createNews(@RequestBody NewsCreationRequest newsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.saveNews(newsRequest));
    }

    @Operation(description = "Endpoint for fetching all news items",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not authorized",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<NewsDto>> fetchNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @Operation(description = "Endpoint for fetching a specific news item by its ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not authorized",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> fetchOneNew(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }
}
