package com.example.blog.resources.dto.postDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "DTO zum updaten eines Post's")
@Data
public class PostUpdateDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private int duration;
}
