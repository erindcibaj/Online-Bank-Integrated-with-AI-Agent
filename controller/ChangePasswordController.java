package com.example.bank.controller;

import com.example.bank.model.ChangePassword;
import com.example.bank.model.UsersModel;
import com.example.bank.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChangePasswordController {
    
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ChangePasswordController(UsersRepository usersRepository, PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model, HttpSession session) {
        String customerNumber = (String) session.getAttribute("customerNumber"); // Get from session
        
        if (customerNumber == null) {
            return "redirect:/login"; // Redirect if not logged in
        }
        
        ChangePassword changePassword = new ChangePassword();
        changePassword.setCustomerNumber(customerNumber); // Set customer number automatically
        
        model.addAttribute("changePassword", changePassword);
        return "change-password";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute ChangePassword changePassword, HttpSession session, RedirectAttributes redirectAttributes) {
        String customerNumber = (String) session.getAttribute("customerNumber"); // Get from session
        
        if (customerNumber == null) {
            return "redirect:/login";
        }

        Optional<UsersModel> optionalUser = usersRepository.findByCustomerNumber(customerNumber);

        UsersModel usersModel = optionalUser.get(); // Get the actual user object

        if (!passwordEncoder.matches(changePassword.getOldPassword(), usersModel.getPassword())) {
            redirectAttributes.addFlashAttribute("errorOldPassword", "Existing password is incorrect.");
            return "redirect:/change-password";
        }

        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("errorPassword", "New passwords do not match.");
            return "redirect:/change-password";
        }
        
                // Check if new password is the same as the old one
        if (passwordEncoder.matches(changePassword.getNewPassword(), usersModel.getPassword())) {
            redirectAttributes.addFlashAttribute("errorSamePassword", "New password must be different from the old one.");
            return "redirect:/change-password";
        }

        usersModel.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        usersRepository.save(usersModel);

        redirectAttributes.addFlashAttribute("successPassword", "Password changed successfully.");
        
        return "redirect:/change-password";
    }


    }

