package com.example.blog.resources.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "blog_posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 254)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private int duration;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
