package com.test_task.newsfeed.repo;

import com.test_task.newsfeed.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
