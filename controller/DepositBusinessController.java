
package com.example.bank.controller;

import com.example.bank.model.DepositBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.DepositBusinessService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DepositBusinessController {
    
     private final DepositBusinessService depositBusinessService;
      private final AccountNumberBusinessService accountNumberBusinessService;
        
    public DepositBusinessController(DepositBusinessService depositBusinessService, AccountNumberBusinessService accountNumberBusinessService) {
        this.depositBusinessService = depositBusinessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
     @GetMapping("/deposit-list-business")
    public String getDepositListBusinessPage(Model model, HttpSession session) {
        // Retrieve the logged-in business customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Fetch only the deposits associated with this business customer
        List<DepositBusinessModel> deposit = depositBusinessService.getDepositsByCustomerNumber(customerNumber);
         model.addAttribute("deposit", deposit);
         return "deposit-list-business";
    }
    
    @GetMapping("/new-deposit-business")
       public String getNewDepositBusinessPage(Model model, HttpSession session){
                          // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/loginBusiness";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newDepositBusiness",new DepositBusinessModel());
        return "new-deposit-business";
}
       
       @PostMapping("/new-deposit-business")
        public String createDeposit(@ModelAttribute DepositBusinessModel depositBusinessModel, RedirectAttributes redirectAttributes, HttpSession session) {
            
                    // Retrieve the logged-in business customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");
        
                // Calculate maturity date
        if (depositBusinessModel.getDepositTerm() != null && !depositBusinessModel.getDepositTerm().isBlank()) {
            String termString = depositBusinessModel.getDepositTerm().trim(); // e.g., "12 Months"
            
            // If input is only digits (e.g., "12"), append " Months"
            if (termString.matches("\\d+")) {
                    // Only digits entered (e.g., "1", "12")
                    int number = Integer.parseInt(termString);
                    termString += number == 1 ? " Month" : " Months";} 
            else {
                // Normalize: "months" -> "Months", "month" -> "Month" (case-insensitive)
                termString = termString
                                .replaceAll("(?i)\\bmonths\\b", "Months")  // handles "months"
                                .replaceAll("(?i)\\bmonth\\b", "Month");   // handles "month"
            }

            depositBusinessModel.setDepositTerm(termString); // Save the cleaned version

            // Extract numeric part using regex
            String numberOnly = termString.replaceAll("[^0-9]", ""); // Removes non-numeric characters

            try {
                int months = Integer.parseInt(numberOnly);
                LocalDate maturityDate = LocalDate.now().plusMonths(months);
                depositBusinessModel.setMaturityDate(maturityDate);
            } catch (NumberFormatException e) {
                // Optionally handle invalid input, log, or show error
                System.out.println("Invalid deposit term format: " + termString);
            }
        }

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Assign the logged-in business customer's number to the deposit
        depositBusinessModel.setCustomerNumber(customerNumber);
            
    try {
        depositBusinessService.createDepositBusiness(depositBusinessModel);
        return "redirect:/deposit-list-business";
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("errorDepositBusiness", e.getMessage());
        return "redirect:/new-deposit-business";
        }   
    }
}
