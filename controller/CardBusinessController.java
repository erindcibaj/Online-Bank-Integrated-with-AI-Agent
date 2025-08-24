
package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.CardBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.CardBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CardBusinessController {
    
    private final CardBusinessService cardBusinessService;
    private final AccountNumberBusinessService accountNumberBusinessService;
        
    public CardBusinessController(CardBusinessService cardBusinessService, AccountNumberBusinessService accountNumberBusinessService) {
        this.cardBusinessService = cardBusinessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
@GetMapping("/card-list-business")
public String getCardListPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    // Retrieve the logged-in business customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        redirectAttributes.addFlashAttribute("sessionError", "Your session has expired. Please log in again.");
        return "redirect:/loginBusiness";
    }

    // Fetch business cards linked to this customer
    List<CardBusinessModel> cards = cardBusinessService.getCardsByCustomerNumber(customerNumber);

    // Decrypt card numbers before displaying
    for (CardBusinessModel card : cards) {
        try {
            String decryptedCardNumber = EncryptionUtil.decrypt(card.getCardNumber());
            card.setCardNumber(decryptedCardNumber);
        } catch (Exception e) {
            card.setCardNumber("Error");
        }
    }

    model.addAttribute("cardsBusiness", cards);
    return "card-list-business";
}


    
    @GetMapping("/new-card-business")
    public String getNewCardPage(Model model, HttpSession session){
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/loginBusiness";
    }

    // Fetch accounts linked to this customer
    List<String> accountNumbers = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);

    // Create a new CardModel and set the customerNumber
    CardBusinessModel newCard = new CardBusinessModel();
    newCard.setCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newCardBusiness",newCard);
        return "new-card-business";
    }
    
@PostMapping("/new-card-business")
public String submitNewCard(@ModelAttribute CardBusinessModel cardBusinessModel, RedirectAttributes redirectAttributes, HttpSession session) {
    // Retrieve customer number from session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        redirectAttributes.addFlashAttribute("sessionError", "Session expired. Please log in again.");
        return "redirect:/loginBusiness";
    }

    // Set the customer number
    cardBusinessModel.setCustomerNumber(customerNumber);

    try {
        // Encrypt the card number
        String encryptedCardNumber = EncryptionUtil.encrypt(cardBusinessModel.getCardNumber());
        cardBusinessModel.setCardNumber(encryptedCardNumber);

        // Check if encrypted card number already exists
        if (cardBusinessService.cardExists(encryptedCardNumber)) {
            redirectAttributes.addFlashAttribute("cardError", "Card number already exists.");
            return "redirect:/new-card-business";
        }

        // Save the encrypted card
        cardBusinessService.saveCard(cardBusinessModel);
        return "redirect:/card-list-business";

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("cardError", "An error occurred while processing the card.");
        return "redirect:/new-card-business";
    }
}

}
