
package com.example.bank.controller;

import com.example.bank.model.LoanModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.LoanService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoanController {
  
      private final LoanService loanService;
      private final AccountService accountService;
        
    public LoanController(LoanService loanService, AccountService accountService) {
        this.loanService = loanService;
        this.accountService = accountService;
    }
    
    @GetMapping("/loan-list")
    public String getLoanPage(Model model, HttpSession session) {
        // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/login";
        }

        // Fetch only loans associated with this customer
        List<LoanModel> loans = loanService.getLoansByCustomerNumber(customerNumber);
        model.addAttribute("loans", loans);
        model.addAttribute("loan", new LoanModel());
        return "loan-list";
    }
    
        @GetMapping("/new-loan")
    public String getNewCardPage(Model model, HttpSession session){
                   // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/login";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newLoan",new LoanModel());
        return "new-loan";
    }

       @PostMapping("/new-loan")
        public String submitLoan(@ModelAttribute LoanModel loanModel, HttpSession session) {
            
                    // Retrieve customer number from session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";
    }

    // Set the customer number 
    loanModel.setCustomerNumber(customerNumber);
    
        // Set default description if empty
    if (loanModel.getLoanPurpose() == null || loanModel.getLoanPurpose().trim().isEmpty()) {
        loanModel.setLoanPurpose("Description not provided");
    }

    loanService.saveLoan(loanModel);
    return "redirect:/loan-list";
    }
}
