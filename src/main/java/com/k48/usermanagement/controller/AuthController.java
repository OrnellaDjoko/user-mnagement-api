package com.k48.usermanagement.controller;

import com.k48.usermanagement.dto.LoginRequest;
import com.k48.usermanagement.dto.LoginResponse;
import com.k48.usermanagement.dto.RefreshTokenRequest;
import com.k48.usermanagement.service.AuthService;
import com.k48.usermanagement.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(
        name = "Authentification",
        description = "Authentification des utilisateurs"
)
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

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

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
            ){
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ){
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token
    ){
        emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok("Email vérifié avec succès");
    }

}
