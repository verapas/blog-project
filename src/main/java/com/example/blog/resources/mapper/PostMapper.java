package com.example.blog.resources.mapper;

import com.example.blog.resources.dto.postDto.PostCreateDto;
import com.example.blog.resources.dto.postDto.PostShowDto;
import com.example.blog.resources.dto.postDto.PostUpdateDto;
import com.example.blog.resources.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt")
    PostShowDto toShowDto(Post post);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
//    @Mapping(source = "userId", target = "user.id")
    Post toEntity(PostCreateDto dto);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    void updateEntity(PostUpdateDto dto, @MappingTarget Post post);
}
