package com.k48.usermanagement.service;

import com.k48.usermanagement.dto.LoginRequest;
import com.k48.usermanagement.dto.LoginResponse;
import com.k48.usermanagement.entity.RefreshToken;
import com.k48.usermanagement.entity.User;
import com.k48.usermanagement.exception.EmailNotVerifiedException;
import com.k48.usermanagement.exception.InvalidCredentialsException;
import com.k48.usermanagement.repository.UserRepository;
import com.k48.usermanagement.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Email ou mot de passe incorrect"));
        if(!passwordEncoder.matches(
                request.getPassword(), user.getPassword()
        )){
            throw new InvalidCredentialsException(
                    "Email ou mot de passe incorrect");
        }

        if(!user.isEmailVerified()){
            throw new EmailNotVerifiedException("Veuillez vérifier votre adresse email avant" +
                    "de vous connecter");
        }
        String accessToken = jwtService.generateToken(user.getEmail());

        RefreshToken refreshToken = refreshTokenService.create(user);
        return new LoginResponse(accessToken, refreshToken.getToken());
    }


    public LoginResponse refresh(String token){
        RefreshToken refreshToken = refreshTokenService.validate(token);

        User user =refreshToken.getUser();

        String accessToken = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public void logout(String refreshToken){
        refreshTokenService.revoke(refreshToken);
    }
}