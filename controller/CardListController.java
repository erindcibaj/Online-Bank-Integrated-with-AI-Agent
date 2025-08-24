package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.CardModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.CardService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CardListController {
    
      private final CardService cardService;
      private final AccountService accountService;
        
    public CardListController(CardService cardService, AccountService accountService) {
        this.cardService = cardService;
        this.accountService = accountService;
    }
    
@GetMapping("/card-list")
public String getCardListPage(Model model, HttpSession session) {
    // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        // Redirect to login if session is missing
        return "redirect:/login";
    }

    // Fetch only the cards related to this customer
    List<CardModel> cards = cardService.getCardsByCustomerNumber(customerNumber);

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
    return "card-list";
}

    
    @GetMapping("/new-card")
    public String getNewCardPage(Model model, HttpSession session){
                          // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");
        
        

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/login";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountService.getAccountsByCustomerNumber(customerNumber);
        
            // Create a new CardModel and set the customerNumber
        CardModel newCard = new CardModel();
        newCard.setCustomerNumber(customerNumber);
    
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newCard",newCard);
        return "new-card";
    }
    
@PostMapping("/new-card")
public String submitNewCard(@ModelAttribute CardModel cardModel, RedirectAttributes redirectAttributes, HttpSession session) {

    // Retrieve customer number from session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";
    }

    // Set the customer number in cardModel
    cardModel.setCustomerNumber(customerNumber);

    try {
        // Check if the plain card number already exists (you should encrypt before checking)
        String encryptedCardNumber = EncryptionUtil.encrypt(cardModel.getCardNumber());
        
        if (cardService.cardExists(encryptedCardNumber)) {
            redirectAttributes.addFlashAttribute("cardError", "Card number already exists.");
            return "redirect:/new-card";
        }

        // Encrypt card number before saving
        cardModel.setCardNumber(encryptedCardNumber);

        // Save the card
        cardService.saveCard(cardModel);
        return "redirect:/card-list";

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("cardError", "An error occurred while processing the card.");
        return "redirect:/new-card";
    }
}


}
