package com.k48.usermanagement.service;

import com.k48.usermanagement.entity.EmailVerificationToken;
import com.k48.usermanagement.entity.User;
import com.k48.usermanagement.exception.ResourceNotFoundException;
import com.k48.usermanagement.repository.EmailVerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    @Transactional
    public String createVerificationToken(User user){

        tokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken(
                token,
                user,
                LocalDateTime.now().plusHours(24)
        );

        tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getName(),
                token
        );
        return token;
    }

    @Transactional
    public void verifyEmail(String token){
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de validation invalide"));

        if(verificationToken.isUsed()){
            throw new RuntimeException("Ce token a déjà été utilisé");
        }

        if(verificationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException(
                    "Le token de vérification a expiré"
            );
        }

        User user = verificationToken.getUser();

        user.setEmailVerified(true);
        verificationToken.setUsed(true);
    }
}
