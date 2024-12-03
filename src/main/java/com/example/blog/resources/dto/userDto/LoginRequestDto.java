package com.example.blog.resources.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotNull
    private String username;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
}
