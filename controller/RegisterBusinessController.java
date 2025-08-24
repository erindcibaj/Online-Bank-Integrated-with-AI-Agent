
package com.example.bank.controller;

import com.example.bank.model.BusinessAccount;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.BusinessService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterBusinessController {
    
    private final BusinessService businessService;
    private final AccountNumberBusinessService accountNumberBusinessService;
    
    public RegisterBusinessController(BusinessService businessService, AccountNumberBusinessService accountNumberBusinessService){
        this.businessService = businessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
    // Step 1: Show registration form
    @GetMapping("/registerBusiness")
    public String getRegisterBusinessPage(Model model, HttpSession session) {
        session.removeAttribute("tempBusiness"); // Ensure old session data is cleared
        model.addAttribute("registerRequest", new BusinessAccount());
        return "registerBusiness";
    }

    // Step 1: Store user data in session and redirect to password page
    @PostMapping("/register-business-step1")
    public String handleStep1(@ModelAttribute BusinessAccount businessAccount, RedirectAttributes redirectAttributes,HttpSession session) {
        
                        // Check if the customer number exists in AccountModel
        if (!accountNumberBusinessService.customerExists(businessAccount.getCustomerNumber())) {
            redirectAttributes.addFlashAttribute("errorRegister", "Customer number not found");
            return "redirect:/registerBusiness";
        }
        
        if (businessService.userExists(businessAccount.getCustomerNumber())) {
        redirectAttributes.addFlashAttribute("registerError", "Customer number already exists");
        return "redirect:/registerBusiness";
    }
        
     if (businessService.niptExists(businessAccount.getNipt())) {
        redirectAttributes.addFlashAttribute("niptError", "NIPT already exists");
        return "redirect:/registerBusiness";
    }
     
        if (businessService.emailExists(businessAccount.getEmail())) {
        redirectAttributes.addFlashAttribute("emailError", "Email already exists");
        return "redirect:/registerBusiness";
    }
        
        if (businessService.phoneExists(businessAccount.getPhone())) {
        redirectAttributes.addFlashAttribute("phoneError", "Phone number already exists");
        return "redirect:/registerBusiness";
    }
        
        session.setAttribute("tempBusiness", businessAccount); // Store business data in session
        return "redirect:/passwordBusiness";
    }

    // Step 2: Show password input page
    @GetMapping("/passwordBusiness")
    public String getPasswordPage(Model model, HttpSession session) {
        BusinessAccount tempBusiness = (BusinessAccount) session.getAttribute("tempBusiness");

        if (tempBusiness == null) {
            return "redirect:/registerBusiness"; // Redirect if no session data exists
        }

        model.addAttribute("registerRequest", tempBusiness);
        return "passwordBusiness";
    }

    // Final Step: Save user data
   @PostMapping("/registerBusiness")
public String register(@ModelAttribute BusinessAccount businessAccount, HttpSession session, RedirectAttributes redirectAttributes) {
    BusinessAccount tempBusiness = (BusinessAccount) session.getAttribute("tempBusiness");

    if (tempBusiness != null) {
        
        //  Check if password and confirmPassword match
        if (!businessAccount.getPassword().equals(businessAccount.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("errorPassword", "Passwords do not match!");
            return "redirect:/passwordBusiness";  // Stay on password page and show error
        }
        
        tempBusiness.setPassword(businessAccount.getPassword()); // Set password from form
        tempBusiness.setConfirmPassword(businessAccount.getConfirmPassword());

         // Save business to the database
        BusinessAccount registeredBusiness = businessService.registerBusiness(tempBusiness);

        session.removeAttribute("tempBusiness"); // Clear session data after registration

        if (registeredBusiness != null) {
            return "redirect:/loginBusiness";  // Successful registration
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed. Please try again.");
            redirectAttributes.addFlashAttribute("registerRequest", tempBusiness); // Retain user input
            return "redirect:/passwordBusiness";  // Return to password page with error prompt
        }
    }

    redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please start the registration again.");
    return "redirect:/registerBusiness";  // Return to registration page if session is invalid
}

}
