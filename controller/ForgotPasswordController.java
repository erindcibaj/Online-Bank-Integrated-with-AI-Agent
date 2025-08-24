
package com.example.bank.controller;

import com.example.bank.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {
    
    
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(){
        return "forgot-password";
    }
    
        @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
    boolean emailExists = forgotPasswordService.processForgotPassword(email);

    if (emailExists) {
        redirectAttributes.addFlashAttribute("message", "Reset link has been sent.");
    } else {
        redirectAttributes.addFlashAttribute("errorEmail", "Email not found. Please enter a valid email.");
    }

    return "redirect:/forgot-password";    
    }
}
