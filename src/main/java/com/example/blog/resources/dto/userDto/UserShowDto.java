package com.example.blog.resources.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zur Anzeige einer Übersicht eines Users")
@Data
public class UserShowDto {

    @NotNull
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String role;
}
