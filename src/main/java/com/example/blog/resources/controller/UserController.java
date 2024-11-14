package com.example.blog.resources.controller;

import com.example.blog.resources.dto.userDto.UserCreateDto;
import com.example.blog.resources.dto.userDto.UserDetailDto;
import com.example.blog.resources.dto.userDto.UserShowDto;
import com.example.blog.resources.dto.userDto.UserUpdateDto;
import com.example.blog.resources.entity.User;
import com.example.blog.resources.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Verwaltet CRUD-Operationen für User
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * Gibt einen spezifischen Benutzer anhand der ID zurück.
     * URL: GET http://localhost:8080/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserShowDto> getUserById(@PathVariable Long id) {
        UserShowDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }


    /**
     * Registriert einen neuen Benutzer.
     * URL: POST http://localhost:8080/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserShowDto> registerUser(@RequestBody UserCreateDto userDto) {
        UserShowDto registeredUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    /**
     * Aktualisiert einen bestehenden Benutzer anhand der ID.
     * URL: PUT http://localhost:8080/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserShowDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        UserShowDto updatedUser = userService.update(id, userDto);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedUser);
    }


    /**
     * Löscht einen Benutzer anhand der ID.
     * URL: DELETE http://localhost:8080/users/{id}
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        UserShowDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

