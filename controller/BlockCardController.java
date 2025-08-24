package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.BlockCardModel;
import com.example.bank.service.BlockCardService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlockCardController {
    
    private final BlockCardService blockCardService;
    
        public BlockCardController(BlockCardService blockCardService) {
        this.blockCardService = blockCardService;
    }
    
    @GetMapping("/block-card")
    public String getBlockCardPage(Model model, HttpSession session){
            // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";  // Redirect if no customer number found
    }

    // Fetch encrypted card numbers
    List<String> encryptedCardNumbers = blockCardService.getCardNumbersByCustomerNumber(customerNumber);

    // Decrypt card numbers
    List<String> decryptedCardNumbers = encryptedCardNumbers.stream().map(cardNumber -> {
        try {
            return EncryptionUtil.decrypt(cardNumber);
        } catch (Exception e) {
            return "Error";
        }
    }).toList();
    
        model.addAttribute("card",decryptedCardNumbers);
        model.addAttribute("blockCard", new BlockCardModel());
        return "block-card";
    }
    
    @PostMapping("/block-card")
        public String submitCardApplication(@ModelAttribute BlockCardModel blockCardModel){
    
    blockCardService.saveBlockCard(blockCardModel);
    return "redirect:/dashboard";
        }
    
}
