package com.example.bank.controller;

import com.example.bank.model.UsersModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {
    
    private final UsersService usersService;
    private final AccountService accountService;

    public RegisterController(UsersService usersService, AccountService accountService) {
        this.usersService = usersService;
        this.accountService = accountService;
    }
    
    @GetMapping("/register")
public String getRegisterPage(String customerNumber, Model model, HttpSession session) {
    session.removeAttribute("tempUser"); 
    model.addAttribute("registerRequest", new UsersModel());
    return "register";
}


    // Step 1: Store user data in session and redirect to password page
    @PostMapping("/register-step1")
    public String handleStep1(@ModelAttribute UsersModel usersModel, RedirectAttributes redirectAttributes, HttpSession session) {
        
                // Check if the customer number exists in AccountModel
        if (!accountService.customerExists(usersModel.getCustomerNumber())) {
            redirectAttributes.addFlashAttribute("errorRegister", "Customer number not found");
            return "redirect:/register";
        }
        
         if (usersService.userExists(usersModel.getCustomerNumber())) {
        redirectAttributes.addFlashAttribute("registerError", "Customer number already exists.");
        return "redirect:/register";
    }   
         
         if (usersService.emailExists(usersModel.getEmail())) {
        redirectAttributes.addFlashAttribute("emailError", "Email already exists.");
        return "redirect:/register";
    }
         
            if (usersService.phoneExists(usersModel.getPhone())) {
        redirectAttributes.addFlashAttribute("phoneError", "Phone number already exists.");
        return "redirect:/register";
    }

        session.setAttribute("tempUser", usersModel); // Store data in session
        return "redirect:/password";
    }

    // Step 2: Show password input page
    @GetMapping("/password")
    public String getPasswordPage(Model model, HttpSession session) {
        UsersModel tempUser = (UsersModel) session.getAttribute("tempUser");

        if (tempUser == null) {
            return "redirect:/register"; // Redirect if no session data exists
        }

        model.addAttribute("registerRequest", tempUser);
        return "password";
    }

    // Final Step: Save user data
   @PostMapping("/register")
public String register(@ModelAttribute UsersModel usersModel, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    UsersModel tempUser = (UsersModel) session.getAttribute("tempUser");
    
    
    
    if (tempUser != null) {
        
                // Check if password and confirmPassword match
        if (!usersModel.getPassword().equals(usersModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("errorPassword", "Passwords do not match!");
            return "redirect:/password";  // Stay on password page and show error
        }
        
        tempUser.setPassword(usersModel.getPassword()); // Set password from form
        tempUser.setConfirmPassword(usersModel.getConfirmPassword());
    
            // Save user to the database
        UsersModel registeredUser = usersService.registerUser(tempUser);


        session.removeAttribute("tempUser"); // Clear session data after registration

        if (registeredUser != null) {
            return "redirect:/login";  // Successful registration
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Registration failed. Please try again.");
            redirectAttributes.addFlashAttribute("registerRequest", tempUser); // Retain user input
            return "redirect:/password";  // Return to password page with error prompt
        }
    }
 
    redirectAttributes.addFlashAttribute("errorMsg", "Session expired. Please start the registration again.");
    return "redirect:/register";  // Return to registration page if session is invalid
}
}
