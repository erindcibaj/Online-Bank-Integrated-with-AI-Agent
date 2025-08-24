package com.example.bank.controller;

import com.example.bank.model.UsersModel;
import com.example.bank.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }
    
    // Redirect root to login
    @GetMapping("/")
    public String redirectToLogin() {
    return "redirect:/login";
    }

    // Step 1: Show login page
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new UsersModel());
        return "login";
    }
    
    
    // Handle login 
@PostMapping("/login")
public String login(@ModelAttribute UsersModel usersModel, HttpSession session, RedirectAttributes redirectAttributes) {
        // Authenticate user using customer number and password
        UsersModel authenticated = usersService.authenticate(usersModel.getCustomerNumber(), usersModel.getPassword());

        if (authenticated != null) {
            // Store customer number in session for later use
            session.setAttribute("customerNumber", usersModel.getCustomerNumber());

            // Redirect based on user role (admin or customer)
            if (authenticated.getAdmin() == 1) {
                return "redirect:/admin-dashboard"; // Redirect to admin dashboard
            } else {
                return "redirect:/dashboard"; // Redirect to customer dashboard
            }
        } else {
            // If authentication fails, return to login page with error message
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid credentials. Please try again.");
            return "redirect:/login"; // Stay on login page
        }
    }
}

    

