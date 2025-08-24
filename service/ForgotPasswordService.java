
package com.example.bank.service;

import com.example.bank.model.UsersModel;
import com.example.bank.repository.UsersRepository;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsersRepository usersRepository;

    public boolean processForgotPassword(String email) {
        Optional<UsersModel> userOpt = usersRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            UsersModel usersModel = userOpt.get();
            String token = UUID.randomUUID().toString();
            usersModel.setResetToken(token);
            usersRepository.save(usersModel);

            sendResetEmail(usersModel.getEmail(), token);
            return true;
        }
        return false;
    }

    private void sendResetEmail(String email, String token) {
 try {
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String body = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to reset your password:</p>"
                    + "<p><a href=\"" + resetLink + "\">Change my password</a></p>"
                    + "<p>If you did not request this, please ignore this email.</p>"
                    + "<p>Thank you!</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setFrom("contact@bank.com","Bank Support");
        helper.setSubject(subject);
        helper.setText(body, true); // Enable HTML content

        mailSender.send(message);
    } catch (Exception e) {
        e.printStackTrace();
        }
    }
}
