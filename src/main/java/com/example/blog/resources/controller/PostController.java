package com.example.blog.resources.controller;

import com.example.blog.resources.dto.postDto.PostCreateDto;
import com.example.blog.resources.dto.postDto.PostShowDto;
import com.example.blog.resources.dto.postDto.PostUpdateDto;
import com.example.blog.resources.entity.Post;
import com.example.blog.resources.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostShowDto>> getAllPosts() {
        List<PostShowDto> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostShowDto> getPostById(@PathVariable Long id) {
        PostShowDto post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostShowDto> createPost(@RequestBody PostCreateDto postDto) {
        PostShowDto createdPost = postService.create(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PostShowDto> updatePost(@PathVariable Long id, @RequestBody PostUpdateDto postDto) {
        PostShowDto updatedPost = postService.update(id, postDto);
        if (updatedPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        PostShowDto post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
