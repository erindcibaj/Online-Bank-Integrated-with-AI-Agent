    package com.example.bank.controller;

    import com.example.bank.model.UsersModel;
    import com.example.bank.repository.UsersRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    import java.util.Optional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    @Controller
    public class ResetPasswordController {

        @Autowired
        private UsersRepository usersRepository;

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;  // Using a single instance of BCryptPasswordEncoder

        @GetMapping("/reset-password")
        public String getResetPasswordPage(@RequestParam("token") String token, Model model) {
            // Check if the token is present in the database
            Optional<UsersModel> userOpt = usersRepository.findByResetToken(token);

            if (userOpt.isPresent()) {
                // Token exists, pass it to the frontend
                model.addAttribute("token", token);
                return "reset-password";
            } else {
                // Invalid or expired token
                model.addAttribute("errorToken", "Invalid or expired token.");
                return "reset-password";  
            }
        }

        @PostMapping("/reset-password")
        public String resetPassword(@RequestParam("token") String token,
                                    @RequestParam("password") String newPassword,
                                    @RequestParam("confirmPassword") String confirmPassword,
                                     RedirectAttributes redirectAttributes) {

                // Step 1: Check if password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorResetPassword", "Passwords do not match.");
            redirectAttributes.addFlashAttribute("token", token);  // Add token back to model
            return "redirect:/reset-password";  // Return to reset-password page with error
        }

            // Check if the token is present in the database
            Optional<UsersModel> userOpt = usersRepository.findByResetToken(token);

            if (userOpt.isPresent()) {
                UsersModel usersModel = userOpt.get();
                
                        // Step 3: Check if the new password is the same as the old one
        if (bCryptPasswordEncoder.matches(newPassword, usersModel.getPassword())) {
            redirectAttributes.addFlashAttribute("errorResetPassword", "New password is the same as old password.");
            redirectAttributes.addFlashAttribute("token", token);
            return "redirect:/reset-password";
        }

                try {
                    // Encrypt the new password and set it to the user object
                    usersModel.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    usersModel.setResetToken(null); // Remove the token after reset

                    // Save the updated user object
                    usersRepository.save(usersModel);

                    // Password reset successful
                    return "login";  // Redirect to login page
                } catch (Exception e) {
                    // Log the error and return a failure message
                    redirectAttributes.addFlashAttribute("errorResetPassword", "Error resetting the password. Please try again.");
                    return "redirect:/reset-password"; 
                }
            } else {
                // Token is invalid or expired
                redirectAttributes.addFlashAttribute("errorToken", "Invalid or expired token.");
                return "redirect:/reset-password";  
            }
        }
    }
