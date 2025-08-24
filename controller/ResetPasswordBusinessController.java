package com.example.bank.controller;

import com.example.bank.model.BusinessAccount;
import com.example.bank.repository.BusinessRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResetPasswordBusinessController {
    
     @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;  // Using a single instance of BCryptPasswordEncoder

    @GetMapping("/reset-password-business")
    public String getResetPasswordPage(@RequestParam("token") String token, Model model) {
        // Check if the token is present in the database
        Optional<BusinessAccount> userOpt = businessRepository.findByResetToken(token);

        if (userOpt.isPresent()) {
            // Token exists, pass it to the frontend
            model.addAttribute("token", token);
            return "reset-password-business";
        } else {
            // Invalid or expired token
            model.addAttribute("errorToken", "Invalid or expired token.");
            return "reset-password-business";  
        }
    }

    @PostMapping("/reset-password-business")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        
                    // Step 1: Check if password and confirm password match
    if (!newPassword.equals(confirmPassword)) {
        redirectAttributes.addFlashAttribute("errorResetPassword", "Passwords do not match.");
        redirectAttributes.addFlashAttribute("token", token);  // Add token back to model
        return "redirect:/reset-password-business";  // Return to reset-password page with error
    }

        // Check if the token is present in the database
        Optional<BusinessAccount> userOpt = businessRepository.findByResetToken(token);

        if (userOpt.isPresent()) {
            BusinessAccount businessAccount = userOpt.get();
            
                                    // Step 3: Check if the new password is the same as the old one
        if (bCryptPasswordEncoder.matches(newPassword, businessAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("errorResetPassword", "New password is the same as old password.");
            redirectAttributes.addFlashAttribute("token", token);
            return "redirect:/reset-password-business";
        }

            try {
                // Encrypt the new password and set it to the user object
                businessAccount.setPassword(bCryptPasswordEncoder.encode(newPassword));
                businessAccount.setResetToken(null); // Remove the token after reset

                // Save the updated user object
                businessRepository.save(businessAccount);

                // Password reset successful
                return "loginBusiness";  // Redirect to login page
            } catch (Exception e) {
                // Log the error and return a failure message
                redirectAttributes.addFlashAttribute("errorResetPassword", "Error resetting the password. Please try again.");
                return "redirect:/reset-password-business"; 
            }
        } else {
            // Token is invalid or expired
            redirectAttributes.addFlashAttribute("errorToken", "Invalid or expired token.");
            return "redirect:/reset-password-business";  
        }
    }
}
