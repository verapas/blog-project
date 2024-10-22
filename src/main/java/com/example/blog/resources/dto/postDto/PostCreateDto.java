package com.example.blog.resources.dto.postDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zum erstellen eines Post's")
@Data
public class PostCreateDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private int duration;

    @NotNull
    private Long userId;

    private String createdAt;
}
