
package com.example.bank.controller;

import com.example.bank.model.CardApplicationBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.CardApplicationBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CardApplicationBusinessController {
    
     private final CardApplicationBusinessService cardApplicationBusinessService;
     private final AccountNumberBusinessService accountNumberBusinessService;
        
    public CardApplicationBusinessController(CardApplicationBusinessService cardApplicationBusinessService, AccountNumberBusinessService accountNumberBusinessService) {
        this.cardApplicationBusinessService = cardApplicationBusinessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
    @GetMapping("/card-application-business")
    public String getCardApplicationBusinessPage(Model model, HttpSession session){
                   // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/loginBusiness";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("cardApplicationBusiness", new CardApplicationBusinessModel());
        return "card-application-business";
        }
    
    @PostMapping("/card-application-business")
    public String submitCardApplication(@ModelAttribute CardApplicationBusinessModel cardApplicationBusinessModel){
        
    cardApplicationBusinessService.saveApplication(cardApplicationBusinessModel);
    return "redirect:/business-dashboard";
        }
}
