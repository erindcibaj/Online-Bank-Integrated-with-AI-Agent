package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.UnblockCardModel;
import com.example.bank.service.UnblockCardService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UnblockCardController {
    
     private final UnblockCardService unBlockCardService;
    
        public UnblockCardController(UnblockCardService unBlockCardService) {
        this.unBlockCardService = unBlockCardService;
    }
    
    @GetMapping("/unblock-card")
    public String getUnblockCardPage(Model model, HttpSession session){
                    // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";  // Redirect if no customer number found
    }

    // Fetch encrypted card numbers
    List<String> encryptedCardNumbers = unBlockCardService.getCardNumbersByCustomerNumber(customerNumber);

    // Decrypt card numbers
    List<String> decryptedCardNumbers = encryptedCardNumbers.stream().map(cardNumber -> {
        try {
            return EncryptionUtil.decrypt(cardNumber);
        } catch (Exception e) {
            return "Error";
        }
    }).toList();
    
        model.addAttribute("card",decryptedCardNumbers);
        model.addAttribute("unblockCard", new UnblockCardModel());
        return "unblock-card";
    }
    
    @PostMapping("/unblock-card")
        public String submitCardApplication(@ModelAttribute UnblockCardModel unBlockCardModel, BindingResult result){
    
    unBlockCardService.saveUnblockCard(unBlockCardModel);
    return "redirect:/dashboard";
        }
    
}


