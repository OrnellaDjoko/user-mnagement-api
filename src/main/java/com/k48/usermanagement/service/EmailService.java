package com.k48.usermanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email,
                                      String name,
                                      String token){
        String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Vérification de votre adresse email");
        message.setText(
                "Bonjour " + name + ",\n\n"
                        + "Merci pour votre inscription.\n\n"
                        + "Cliquez sur le lien suivant pour vérifier "
                        + "votre adresse email :\n\n"
                        + verificationUrl
                        + "\n\n"
                        + "Ce lien est valable pendant 24 heures."
                        + "\n\n"
                        + "Cordialement,\n"
                        + "User Management API"
        );

        mailSender.send(message);

    }
}
