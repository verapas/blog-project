package com.example.blog.resources.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zur Anzeige einer Übersicht eines Users")
@Data
public class UserShowDto {

    private Long id;
    private String username;
    private String email;
    private String role;
}
