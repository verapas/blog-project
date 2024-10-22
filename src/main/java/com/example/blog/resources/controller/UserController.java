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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserShowDto> getUserById(@PathVariable Long id) {
        UserShowDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserShowDto> registerUser(@RequestBody UserCreateDto userDto) {
        UserShowDto registeredUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserShowDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userDto) {
        UserShowDto updatedUser = userService.update(id, userDto);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedUser);
    }

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

