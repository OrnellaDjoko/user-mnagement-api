package com.k48.usermanagement.service;

import com.k48.usermanagement.dto.*;
import com.k48.usermanagement.entity.RefreshToken;
import com.k48.usermanagement.entity.User;
import com.k48.usermanagement.exception.ConflictException;
import com.k48.usermanagement.exception.EmailNotVerifiedException;
import com.k48.usermanagement.exception.InvalidCredentialsException;
import com.k48.usermanagement.exception.ResourceNotFoundException;
import com.k48.usermanagement.repository.UserRepository;
import com.k48.usermanagement.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public UserResponse create(UserCreateRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new ConflictException("Un utilisateur existe déjà avec cet email");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        emailVerificationService.createVerificationToken(savedUser);

        return UserResponse.fromEntity(savedUser);
    }

    public List<UserResponse> findAll(){
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        return UserResponse.fromEntity(user);
    }

    public UserResponse update(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);

    }

    public void delete(Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Utilisateur introuvable");
        }

        userRepository.deleteById(id);
    }



    public UserResponse getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        return UserResponse.fromEntity(user);
    }

}
