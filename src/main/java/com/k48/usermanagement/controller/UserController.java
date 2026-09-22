package com.k48.usermanagement.controller;

import com.k48.usermanagement.dto.UserCreateRequest;
import com.k48.usermanagement.dto.UserResponse;
import com.k48.usermanagement.dto.UserUpdateRequest;
import com.k48.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(
        name = "Utilisateurs",
        description = "Gestion des utilisateurs"
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Créer un utilisateur",
            description = "Crée un nouvel utilisateur avec un mot de passe chiffré avec BCrypt"
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @Operation(
            summary = "Lister les utilisateurs",
            description = "Retourne la liste des utilisateurs"
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(
            summary = "Consulter un utilisateur",
            description = "Retourne un utilisateur à partir de son identifiant"
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            summary = "Modifier un utilisateur",
            description = "Modifie le nom et l'email d'un utilisateur"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserUpdateRequest request){
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Operation(
            summary = "Supprimer un utilisateur",
            description = "Supprime un utilisateur"
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
