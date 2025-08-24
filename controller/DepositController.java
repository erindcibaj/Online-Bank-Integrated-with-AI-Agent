
package com.example.bank.controller;

import com.example.bank.model.DepositModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.DepositService;
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
public class DepositController {
    
    private final DepositService depositService;
    private final AccountService accountService;
        
    public DepositController(DepositService depositService, AccountService accountService) {
        this.depositService = depositService;
        this.accountService = accountService;
    }
    
     @GetMapping("/deposit-list")
    public String getDepositListPage(Model model, HttpSession session) {
        // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/login";
        }

        // Fetch only the deposits associated with this customer
        List<DepositModel> deposit = depositService.getDepositsByCustomerNumber(customerNumber);
         model.addAttribute("deposit", deposit);
         return "deposit-list";
    }
    
    @GetMapping("/new-deposit")
       public String getNewDepositPage(Model model, HttpSession session){
                  // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/login";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newDeposit",new DepositModel());
        return "new-deposit";
    }
       
       @PostMapping("/new-deposit")
        public String createDeposit(@ModelAttribute DepositModel depositModel, RedirectAttributes redirectAttributes, HttpSession session) {
            
        // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");
        
        // Calculate maturity date
        if (depositModel.getDepositTerm() != null && !depositModel.getDepositTerm().isBlank()) {
            String termString = depositModel.getDepositTerm().trim(); // e.g., "12 Months"
            
            
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

            depositModel.setDepositTerm(termString); // Save the cleaned version


            // Extract numeric part using regex
            String numberOnly = termString.replaceAll("[^0-9]", ""); // Removes non-numeric characters

            try {
                int months = Integer.parseInt(numberOnly);
                LocalDate maturityDate = LocalDate.now().plusMonths(months);
                depositModel.setMaturityDate(maturityDate);
            } catch (NumberFormatException e) {

            }
        }


        if (customerNumber == null) {
            return "redirect:/login";
        }

        // Assign the logged-in customer's number to the deposit
        depositModel.setCustomerNumber(customerNumber);
            
    try {
        depositService.createDeposit(depositModel);
        return "redirect:/deposit-list";
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("errorDeposit", e.getMessage());
        return "redirect:/new-deposit";
         }   
    }
}
