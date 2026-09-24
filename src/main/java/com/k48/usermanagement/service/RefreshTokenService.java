package com.k48.usermanagement.service;

import com.k48.usermanagement.entity.RefreshToken;
import com.k48.usermanagement.entity.User;
import com.k48.usermanagement.exception.ResourceNotFoundException;
import com.k48.usermanagement.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_DURATION_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken create(User user){
        refreshTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(
                token,
                user,
                LocalDateTime.now()
                        .plusDays(REFRESH_TOKEN_DURATION_DAYS)
        );

        return  refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validate(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token invalide"));

        if(refreshToken.isRevoked()){
            throw  new ResourceNotFoundException("Refresh token révoqué");
        }

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ResourceNotFoundException("Refresh token expiré");
        }

        return  refreshToken;
    }

    @Transactional
    public void revoke(String token){

        RefreshToken refreshToken = validate(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
