package com.test_task.newsfeed.auth;

import com.test_task.newsfeed.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_gen")
    @SequenceGenerator(name = "token_id_gen", sequenceName = "token_id_seq", allocationSize = 1)
    Long id;

    @Column(unique = true)
    String token;


    boolean revoked;

    boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
}
