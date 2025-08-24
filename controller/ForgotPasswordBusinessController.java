
package com.example.bank.controller;

import com.example.bank.service.ForgotPasswordBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordBusinessController {
    
    @Autowired
    private ForgotPasswordBusinessService forgotPasswordBusinessService;
    
    @GetMapping("/forgot-password-business")
    public String forgotPasswordBusinessPage(){
        return "forgot-password-business";
    }
    
        @PostMapping("/forgot-password-business")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
    boolean emailExists = forgotPasswordBusinessService.processForgotPasswordBusiness(email);

    if (emailExists) {
        redirectAttributes.addFlashAttribute("message", "Reset link has been sent.");
    } else {
        redirectAttributes.addFlashAttribute("errorEmail", "Email not found. Please enter a valid email.");
    }

    return "redirect:/forgot-password"; // Ensure this is the correct view name
    }
}
