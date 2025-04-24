package com.example.blog.resources.service;

import com.example.blog.resources.dto.userDto.LoginRequestDto;
import com.example.blog.resources.entity.User;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.resources.repository.UserRepository;
import com.example.blog.resources.dto.userDto.UserCreateDto;
import com.example.blog.resources.dto.userDto.UserShowDto;
import com.example.blog.resources.dto.userDto.UserUpdateDto;
import com.example.blog.resources.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;



    /**
     * Findet einen Benutzer anhand der ID und gibt ihn als UserShowDto zurück.
     */
    public UserShowDto findById(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer mit der ID " + id + " konnte nicht gefunden werden!"));
        return userMapper.toShowDto(user);
    }

    /**
     * Findet alle Benutzer und gibt sie als Liste von UserShowDto zurück.
     */
    public List<UserShowDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toShowDto)
                .collect(Collectors.toList());
    }

    /**
     * Erstellt einen neuen Benutzer basierend auf den Daten im UserCreateDto. Der Benutzer hat standardmässig "USER" als rolle
     */
    public UserShowDto create(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = this.userRepository.save(user);
        return userMapper.toShowDto(savedUser);
    }

    /**
     * Aktualisiert einen bestehenden Benutzer anhand der ID und der neuen Daten.
     */
    public UserShowDto update(Long id, UserUpdateDto dto) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer mit der ID " + id + " konnte nicht gefunden werden!"));

        userMapper.updateEntity(dto, user);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toShowDto(updatedUser);
    }
    /**
     * Löscht einen Benutzer anhand der ID.
     */
    public void delete(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer mit der ID " + id + " konnte nicht gefunden werden!"));
        userRepository.delete(user);
    }


    /**
     * Findet und gibt das User-Entity anhand des Benutzernamens zurück.
     */
    public User findUserEntityByEmail(String email) {
        return this.userRepository.getByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Benutzer " + email + " konnte nicht gefunden werden!"));
    }

    /**
     * Überprüft die Anmeldedaten eines Benutzers und gibt den Benutzer zurück, wenn die Anmeldedaten korrekt sind.
     */
    public User getUserWithCredentials(LoginRequestDto loginRequestDto) {
        User user = this.findUserEntityByEmail(loginRequestDto.getEmail());
        if (user != null && passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return user;
        }
        return null;
    }

}
