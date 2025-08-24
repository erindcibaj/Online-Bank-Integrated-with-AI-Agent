package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.CardBusinessModel;
import com.example.bank.model.CardModel;
import com.example.bank.service.CardBusinessService;
import com.example.bank.service.CardService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCardController {
    
    private final CardService cardService;
    private final CardBusinessService cardBusinessService;
    
    public AdminCardController(CardService cardService, CardBusinessService cardBusinessService){
        this.cardService = cardService;
        this.cardBusinessService = cardBusinessService;
    }
    
      @GetMapping("/card-personal-admin")
        public String getAllPersonalAccountCard(Model model){
            List<CardModel> cards = cardService.getAllCards(); // Fetch all cards
            
                // Decrypt card numbers before displaying
    for (CardModel card : cards) {
        try {
            String decryptedCardNumber = EncryptionUtil.decrypt(card.getCardNumber());
            card.setCardNumber(decryptedCardNumber);
        } catch (Exception e) {
            card.setCardNumber("Error");
        }
    }
            model.addAttribute("cards", cards);
            return "card-personal-admin";
    }
        
        @GetMapping("/card-business-admin")
        public String getAllBusinessAccountCard(Model model){
            List<CardBusinessModel> cardsBusiness = cardBusinessService.getAllCards();
            
                // Decrypt card numbers before displaying
    for (CardBusinessModel card : cardsBusiness) {
        try {
            String decryptedCardNumber = EncryptionUtil.decrypt(card.getCardNumber());
            card.setCardNumber(decryptedCardNumber);
        } catch (Exception e) {
            card.setCardNumber("Error");
        }
    }
            model.addAttribute("cardsBusiness",cardsBusiness);
            return "card-business-admin";
        }
}
