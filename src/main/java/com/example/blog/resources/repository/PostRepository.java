package com.example.blog.resources.repository;

import com.example.blog.resources.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

//    Die Methode findById(Long id) ist bereits in CrudRepository oder JpaRepository enthalten
//    /**
//     * Benutzerdefinierte Abfrage, um einen Post anhand seiner ID zu finden.
//     */
//    @Query("SELECT p FROM Post p WHERE p.id = :id")
//    Post findById(@Param("id") Long id);

    /**
     * Methode, um alle Posts eines bestimmten Benutzers zu finden.
     */
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findAllByUserId(@Param("userId") Long userId);

    /**
     * Methode, um Posts nach Titel zu durchsuchen.
     */
    List<Post> findByTitleContaining(String title);
}
