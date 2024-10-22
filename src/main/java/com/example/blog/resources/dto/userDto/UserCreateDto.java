package com.example.blog.resources.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zur Erstellung eines neuen Users")
@Data
public class UserCreateDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String role;
}
