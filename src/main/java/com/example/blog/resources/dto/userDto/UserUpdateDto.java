package com.example.blog.resources.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zur Aktualisierung eines bestehenden Users")
@Data
public class UserUpdateDto {

    @NotNull
    private String username;

    private String password;

    @NotNull
    private String role;
}
