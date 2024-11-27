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

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Diese Methode wird automatisch vor dem speichern aufgerufen
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
