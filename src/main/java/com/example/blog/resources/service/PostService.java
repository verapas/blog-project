package com.example.blog.resources.service;

import com.example.blog.resources.dto.postDto.PostCreateDto;
import com.example.blog.resources.dto.postDto.PostShowDto;
import com.example.blog.resources.entity.Post;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.resources.entity.User;
import com.example.blog.resources.repository.PostRepository;
import com.example.blog.resources.dto.postDto.PostUpdateDto;
import com.example.blog.resources.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    /**
     * Findet einen Post anhand der ID und gibt ihn als PostShowDto zurück.
     */
    public PostShowDto findById(Long id) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post mit der ID " + id + " konnte nicht gefunden werden!"));
        return postMapper.toShowDto(post);
    }

    /**
     * Findet alle Posts und gibt sie als Liste von PostShowDto zurück.
     */
    public List<PostShowDto> findAll() {
        return postRepository.findAll().stream()
                .map(postMapper::toShowDto)
                .collect(Collectors.toList());
    }

    /**
     * Erstellt einen neuen Post basierend auf den Daten im PostCreateDto.
     */
    public PostShowDto create(PostCreateDto dto, User user) {
        Post post = postMapper.toEntity(dto);
        post.setUser(user);

        Post savedPost = this.postRepository.save(post);
        return postMapper.toShowDto(savedPost);
    }

    /**
     * Aktualisiert einen bestehenden Post anhand der ID und der neuen Daten.
     */
    public PostShowDto update(Long id, PostUpdateDto dto) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post mit der ID " + id + " konnte nicht gefunden werden!"));
        postMapper.updateEntity(dto, post);
        Post updatedPost = postRepository.save(post);
        return postMapper.toShowDto(updatedPost);
    }

    /**
     * Löscht einen Post anhand der ID.
     */
    public void delete(Long id) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post mit der ID " + id + " konnte nicht gefunden werden!"));
        postRepository.delete(post);
    }

    /**
     * Findet den zuletzt erstellten Post des angegebenen Benutzers.
     */
    public PostShowDto findLatestByUser(Long userId) {
        Post latestPost = postRepository.findTopByUserIdOrderByIdDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Es wurde kein Post für diesen Benutzer gefunden!"));
        return postMapper.toShowDto(latestPost);
    }

}
