package com.k48.usermanagement.controller;

import com.k48.usermanagement.dto.LoginRequest;
import com.k48.usermanagement.dto.LoginResponse;
import com.k48.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(
        name = "Authentification",
        description = "Authentification des utilisateurs"
)
public class AuthController {

    private final UserService.AuthService authService;

    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Authentifie un utilisateur avec son email et son mot de passe et retourne un JWT"
    )

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));

    }

}
