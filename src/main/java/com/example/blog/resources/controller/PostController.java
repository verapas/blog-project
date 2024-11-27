package com.example.blog.resources.controller;

import com.example.blog.resources.dto.postDto.PostCreateDto;
import com.example.blog.resources.dto.postDto.PostShowDto;
import com.example.blog.resources.dto.postDto.PostUpdateDto;
import com.example.blog.resources.entity.Post;
import com.example.blog.resources.entity.User;
import com.example.blog.resources.security.SecurityConfiguration;
import com.example.blog.resources.service.PostService;
import com.example.blog.resources.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Verwaltet CRUD-Operationen für Controller
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    /**
     * Listet alle Posts auf.
     * URL: GET http://localhost:8080/posts
     */
    @Operation(summary = "Listet alle Posts auf.")
    @ApiResponse(responseCode = "200", description = "Erfolgreich Liste alle Posts zurückgegeben.")
    @GetMapping
    public ResponseEntity<List<PostShowDto>> getAllPosts() {
        List<PostShowDto> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    /**
     * Gibt einen spezifischen Post anhand der ID zurück.
     * URL: GET http://localhost:8080/posts/{id}
     */
    @Operation(summary = "Gibt einen spezifischen Post anhand der ID zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post Details erfolgreich zurückgegeben."),
            @ApiResponse(responseCode = "404", description = "Post nicht gefunden.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostShowDto> getPostById(
            @Parameter(description = "ID des Posts, der abgerufen werden soll.", example = "1")
            @PathVariable Long id) {
        PostShowDto post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(post);
    }

    /**
     * Erstellt einen neuen Post.
     * URL: POST http://localhost:8080/posts
     */
    @Operation(summary = "Erstellt einen neuen Post.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post erfolgreich erstellt"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten")
    })
    @PostMapping
    public ResponseEntity<PostShowDto> createPost(
            @Parameter(description = "Details des zu erstellenden Posts.")
            @RequestBody PostCreateDto postDto) {

        //Aktuellen User aus dem Security-Context holen
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        //Benutzer aus der Datenbank laden
        User user = userService.findUserEntityByUsername(loggedInUsername);

        // Post erstellen mit dem authentifizierten Benutzer
        PostShowDto createdPost = postService.create(postDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Aktualisiert einen bestehenden Post anhand der ID.
     * URL: PUT http://localhost:8080/posts/{id}
     */
    @Operation(summary = "Aktualisiert einen bestehenden Post anhand der ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "404", description = "Post nicht gefunden."),
            @ApiResponse(responseCode = "400", description = "Ungültige EIngabedaten")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostShowDto> updatePost(
            @Parameter(description = "ID des zu aktualisierenden Posts", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Aktualisierte Details des Posts")
            @RequestBody PostUpdateDto postDto) {
        PostShowDto updatedPost = postService.update(id, postDto);
        if (updatedPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * Löscht einen Post anhand der ID.
     * URL: DELETE http://localhost:8080/posts/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Löscht einen Post anhand der ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Post nicht gefunden")
    })
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID des zu löschenden Posts", example = "1")
            @PathVariable Long id) {
        PostShowDto post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
