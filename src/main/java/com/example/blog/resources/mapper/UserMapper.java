package com.example.blog.resources.mapper;

import com.example.blog.resources.dto.userDto.UserCreateDto;
import com.example.blog.resources.dto.userDto.UserShowDto;
import com.example.blog.resources.dto.userDto.UserUpdateDto;
import com.example.blog.resources.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    UserShowDto toShowDto(User user);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    User toEntity(UserCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    void updateEntity(UserUpdateDto dto, @MappingTarget User user);
}
