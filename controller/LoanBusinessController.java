
package com.example.bank.controller;

import com.example.bank.model.LoanBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.LoanBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoanBusinessController {
    
    private final LoanBusinessService loanBusinessService;
      private final AccountNumberBusinessService accountNumberBusinessService;
        
    public LoanBusinessController(LoanBusinessService loanBusinessService, AccountNumberBusinessService accountNumberBusinessService) {
        this.loanBusinessService = loanBusinessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
     @GetMapping("/loan-list-business")
    public String getLoanBusinessPage(Model model, HttpSession session) {
        // Retrieve logged-in business customer number
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Fetch only loans associated with the logged-in business customer
        List<LoanBusinessModel> loanBusiness = loanBusinessService.getLoansByCustomerNumber(customerNumber);
        model.addAttribute("loans",loanBusiness);
        model.addAttribute("loanBusiness", new LoanBusinessModel());
        return "loan-list-business";
    }
    
        @GetMapping("/new-loan-business")
    public String getNewCardBusinessPage(Model model, HttpSession session){
                           // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/loginBusiness";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newLoanBusiness",new LoanBusinessModel());
        return "new-loan-business";
    }

       @PostMapping("/new-loan-business")
        public String submitLoan(@ModelAttribute LoanBusinessModel loanBusinessModel, HttpSession session) {
            
                    // Retrieve business customer number from session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Assign the logged-in business customer number to the loan model
        loanBusinessModel.setCustomerNumber(customerNumber);
        
                // Set default description if empty
    if (loanBusinessModel.getLoanPurpose() == null || loanBusinessModel.getLoanPurpose().trim().isEmpty()) {
        loanBusinessModel.setLoanPurpose("Description not provided");
    }
    
    loanBusinessService.saveLoan(loanBusinessModel);
    return "redirect:/loan-list-business";
    }
}
