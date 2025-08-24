
package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.BlockCardBusinessModel;
import com.example.bank.service.BlockCardBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlockCardBusinessController {
    
     private final BlockCardBusinessService blockCardBusinessService;
    
        public BlockCardBusinessController(BlockCardBusinessService blockCardBusinessService) {
        this.blockCardBusinessService = blockCardBusinessService;
    }
    
    @GetMapping("/block-card-business")
    public String getBlockCardBusinessPage(Model model, HttpSession session){
            // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/loginBusiness";  // Redirect if no customer number found
    }

    // Fetch encrypted card numbers
    List<String> encryptedCardNumbers = blockCardBusinessService.getCardNumbersByCustomerNumber(customerNumber);

    // Decrypt card numbers
    List<String> decryptedCardNumbers = encryptedCardNumbers.stream().map(cardNumber -> {
        try {
            return EncryptionUtil.decrypt(cardNumber);
        } catch (Exception e) {
            return "Error";
        }
    }).toList();
    
        model.addAttribute("card",decryptedCardNumbers);
        model.addAttribute("blockCardBusiness", new BlockCardBusinessModel());
        return "block-card-business";
    }
    
    @PostMapping("/block-card-business")
        public String submitCardApplication(@ModelAttribute BlockCardBusinessModel blockCardBusinessModel){
    
    blockCardBusinessService.saveBlockCard(blockCardBusinessModel);
    return "redirect:/business-dashboard";
        }
}
