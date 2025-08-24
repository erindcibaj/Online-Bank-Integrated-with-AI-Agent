
package com.example.bank.controller;

import com.example.bank.model.BusinessAccount;
import com.example.bank.model.ChangePassword;
import com.example.bank.repository.BusinessRepository;
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
public class ChangePasswordBusinessController {
    
     private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ChangePasswordBusinessController(BusinessRepository businessRepository, PasswordEncoder passwordEncoder){
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/change-password-business")
    public String showChangePasswordBusinessPage(Model model, HttpSession session) {
        String customerNumber = (String) session.getAttribute("customerNumber"); // Get from session
        
        if (customerNumber == null) {
            return "redirect:/loginBusiness"; // Redirect if not logged in
        }
        
        ChangePassword changePassword = new ChangePassword();
        changePassword.setCustomerNumber(customerNumber); // Set customer number automatically
        
        model.addAttribute("changePassword", changePassword);
        return "change-password-business";
    }
    
    @PostMapping("/change-password-business")
    public String changePassword(@ModelAttribute ChangePassword changePassword, HttpSession session, RedirectAttributes redirectAttributes) {
        String customerNumber = (String) session.getAttribute("customerNumber"); // Get from session
        
        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        Optional<BusinessAccount> optionalUser = businessRepository.findByCustomerNumber(customerNumber);

        BusinessAccount businessAccount = optionalUser.get(); // Get the actual user object

        if (!passwordEncoder.matches(changePassword.getOldPassword(), businessAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("errorOldPassword", "Existing password is incorrect.");
            return "redirect:/change-password-business";
        }

        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("errorPassword", "New passwords do not match.");
            return "redirect:/change-password-business";
        }
        
                        // Check if new password is the same as the old one
        if (passwordEncoder.matches(changePassword.getNewPassword(), businessAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("errorSamePassword", "New password must be different from the old one.");
            return "redirect:/change-password";
        }

        businessAccount.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        businessRepository.save(businessAccount);
        
        redirectAttributes.addFlashAttribute("successPassword", "Password changed successfully.");

        return "redirect:/change-password-business";
    }


}
