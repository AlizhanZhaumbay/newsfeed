package com.test_task.newsfeed.controller;

import com.test_task.newsfeed.dto.NewsDto;
import com.test_task.newsfeed.network.NewsCreationRequest;
import com.test_task.newsfeed.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<Long> createNews(@RequestBody NewsCreationRequest newsRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.saveNews(newsRequest));
    }

    @GetMapping
    public ResponseEntity<List<NewsDto>> fetchNews(){
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> fetchOneNew(@PathVariable("id") Long id){
        return ResponseEntity.ok(newsService.getNewsById(id));
    }
}
