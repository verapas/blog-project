package com.example.blog.resources.dto.postDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zur Anzeige einer Übersicht eines Post's")
@Data
public class PostShowDto {

    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private int duration;

    @NotNull
    private Long userId;

    @NotNull
    private String createdAt;
}
