package com.example.bank.controller;

import com.example.bank.model.CardApplicationModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.CardApplicationService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CardApplicationController {
    
      private final CardApplicationService cardApplicationService;
      private final AccountService accountService;
        
    public CardApplicationController(CardApplicationService cardApplicationService, AccountService accountService) {
        this.cardApplicationService = cardApplicationService;
        this.accountService = accountService;
    }
    
    @GetMapping("/card-application")
    public String getCardApplication(Model model, HttpSession session){
           // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/login";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountService.getAccountsByCustomerNumber(customerNumber);
        
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("cardApplication", new CardApplicationModel());
        return "card-application";
        }
    
    @PostMapping("/card-application")
    public String submitCardApplication(@ModelAttribute CardApplicationModel cardApplicationModel){
    
    cardApplicationService.saveApplication(cardApplicationModel);
    return "redirect:/dashboard";
        }
}
