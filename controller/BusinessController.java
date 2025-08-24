package com.example.bank.controller;

import com.example.bank.model.BusinessAccount;
import com.example.bank.service.BusinessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }
    
    // Step 1: Show login page
    @GetMapping("/loginBusiness")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new BusinessAccount());
        return "loginBusiness";
    }

    // Handle login 
    @PostMapping("/loginBusiness")
public String login(@ModelAttribute BusinessAccount businessAccount, Model model, HttpSession session, RedirectAttributes redirectAtrributes) {
    BusinessAccount authenticated = businessService.authenticate(businessAccount.getCustomerNumber(), businessAccount.getPassword());

    if (authenticated != null) {
        session.setAttribute("customerNumber", businessAccount.getCustomerNumber()); // Store customer number in session
        return "redirect:/dashboard-business"; // Redirect to dashboard
    } else {
        redirectAtrributes.addFlashAttribute("errorMessage", "Invalid credentials. Please try again.");
        return "redirect:/loginBusiness";
    }
}
}
