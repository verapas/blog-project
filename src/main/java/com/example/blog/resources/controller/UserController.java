package com.example.blog.resources.controller;

import com.example.blog.resources.dto.userDto.*;
import com.example.blog.resources.entity.User;
import com.example.blog.resources.security.TokenService;
import com.example.blog.resources.security.TokenWrapper;
import com.example.blog.resources.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Verwaltet CRUD-Operationen für User
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;


    /**
     * Gibt einen spezifischen Benutzer anhand der ID zurück.
     * URL: GET http://localhost:8080/users/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Gibt einen spezifischen Benutzer anhand der ID zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benutzerdetails erfolgreich zurükgegeben."),
            @ApiResponse(responseCode = "404", description = "Benutzer nicht gefunden.")
    })
    public ResponseEntity<UserShowDto> getUserById(
            @Parameter(description = "ID des Benutzers, der abgerufen werden soll", example = "1")
            @PathVariable Long id) {
        UserShowDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }


    /**
     * Registriert einen neuen Benutzer.
     * URL: POST http://localhost:8080/users/register
     */
    @PostMapping("/register")
    @Operation(summary = "Registriert einen neuen Benutzer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Benutzer erfolgreich Registriert."),
            @ApiResponse(responseCode = "400", description = "ungültige Eingabedaten.")
    })
    public ResponseEntity<UserShowDto> registerUser(
            @Parameter(description = "Details des zu erstellenden Benutzers.")
            @RequestBody UserCreateDto userDto) {
        UserShowDto registeredUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    /**
     * Aktualisiert einen bestehenden Benutzer anhand der ID.
     * URL: PUT http://localhost:8080/users/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Aktualisiert einen bestehenden Benutzer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benutzer erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "404", description = "Benutzer nicht gefunden."),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten.")
    })
    public ResponseEntity<UserShowDto> updateUser(
            @Parameter(description = "ID des zu aktualisierenden Benutzers", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Aktualisiert Details des Benutzers")
            @RequestBody UserUpdateDto userDto) {
        UserShowDto updatedUser = userService.update(id, userDto);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedUser);
    }


    /**
     * Löscht einen Benutzer anhand der ID.
     * URL: DELETE http://localhost:8080/users/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Löscht einen Benutzer anhand der ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Benutzer erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Benutzer nicht gefunden")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID des zu löschenden Benutzers", example = "1")
            @PathVariable Long id) {
        UserShowDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login")
    @Operation(summary = "Authentifiziert den Benutzer", description = "überprüft die Anmeldedaten und gibt einen JWT zurück, sofern die Authentifizierung gelingt")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Anmeldung erfolgreich, Token zurückgegeben"),
            @ApiResponse(responseCode = "401", description = "Ungültige Anmeldedaten")
    })
    public ResponseEntity<TokenWrapper> login(
            @Parameter(description = "Anmeldedaten des Benutzers")
            @RequestBody LoginRequestDto loginRequestDto) {
        User user = this.userService.getUserWithCredentials(loginRequestDto);
        if (user != null) {
            TokenWrapper tokenWrapper = new TokenWrapper();
            String token = this.tokenService.generateToken(user);
            tokenWrapper.setToken(token);
            return ResponseEntity.ok(tokenWrapper);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

