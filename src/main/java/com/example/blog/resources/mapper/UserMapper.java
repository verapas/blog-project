package com.example.blog.resources.mapper;

import com.example.blog.resources.dto.userDto.UserCreateDto;
import com.example.blog.resources.dto.userDto.UserShowDto;
import com.example.blog.resources.dto.userDto.UserUpdateDto;
import com.example.blog.resources.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "role", target = "role")
    UserShowDto toShowDto(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    User toEntity(UserCreateDto dto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    void updateEntity(UserUpdateDto dto, @MappingTarget User user);
}
