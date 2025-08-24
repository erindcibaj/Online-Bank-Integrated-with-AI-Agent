
package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.UnblockCardBusinessModel;
import com.example.bank.service.UnblockCardBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UnblockCardBusinessController {
    
    private final UnblockCardBusinessService unBlockCardBusinessService;
    
        public UnblockCardBusinessController(UnblockCardBusinessService unBlockCardBusinessService) {
        this.unBlockCardBusinessService = unBlockCardBusinessService;
    }
    
    @GetMapping("/unblock-card-business")
    public String getUnblockCardBusinessPage(Model model, HttpSession session){
                            // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";  // Redirect if no customer number found
    }

    // Fetch encrypted card numbers
    List<String> encryptedCardNumbers = unBlockCardBusinessService.getCardNumbersByCustomerNumber(customerNumber);

    // Decrypt card numbers
    List<String> decryptedCardNumbers = encryptedCardNumbers.stream().map(cardNumber -> {
        try {
            return EncryptionUtil.decrypt(cardNumber);
        } catch (Exception e) {
            return "Error";
        }
    }).toList();
    
        model.addAttribute("card",decryptedCardNumbers);
        model.addAttribute("unblockCardBusiness", new UnblockCardBusinessModel());
        return "unblock-card-business";
    }
    
    @PostMapping("/unblock-card-business")
        public String submitCardApplication(@ModelAttribute UnblockCardBusinessModel unBlockCardBusinessModel, BindingResult result){
    
    unBlockCardBusinessService.saveUnblockCard(unBlockCardBusinessModel);
    return "redirect:/business-dashboard";
        }
}
