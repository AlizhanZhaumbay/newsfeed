package com.test_task.newsfeed.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "news")
public class News extends AbstractEntity {
    @Column(nullable = false, length = 20)
    String product;

    @Column(nullable = false)
    double price;

    @Column(nullable = false, length = 50)
    String destination;

    //Several formats
    @Column(nullable = false, length = 20)
    String phoneNumber;

    @Column(nullable = false)
    String userLogin;
}
