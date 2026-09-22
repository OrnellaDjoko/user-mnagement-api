package com.k48.usermanagement.service;

import com.k48.usermanagement.dto.*;
import com.k48.usermanagement.entity.User;
import com.k48.usermanagement.repository.UserRepository;
import com.k48.usermanagement.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(UserCreateRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

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
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.fromEntity(user);
    }

    public UserResponse update(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);

    }

    public void delete(Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }

    @Service
    @AllArgsConstructor
    public static class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        public LoginResponse login(LoginRequest request){
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Email or password incorrect"));
            if(!passwordEncoder.matches(
                    request.getPassword(), user.getPassword()
            )){
                throw new RuntimeException("Email or password incorrect");
            }

            String token = jwtService.generateToken(user.getEmail());
            return new LoginResponse(token);
        }
    }
}
