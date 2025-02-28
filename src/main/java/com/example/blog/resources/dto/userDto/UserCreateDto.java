package com.example.blog.resources.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zur Erstellung eines neuen Users")
@Data
public class UserCreateDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Automatisch auf 'USER' gesetzt")
    private String role;
}
