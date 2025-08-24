package com.example.bank.service;

import com.example.bank.model.CardModel;
import com.example.bank.repository.CardRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CardService {
  
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    
        public CardModel saveCard(CardModel cardModel){
        
        return cardRepository.save(cardModel);
        
    }
        
          public List<CardModel> getAllCards() {
        return cardRepository.findAll(); // Fetch all cards
    }
          
          public boolean cardExists(String cardNumber){
              return cardRepository.findByCardNumber(cardNumber).isPresent();
          }
          
        public Map<String, Long> countAccountsByCardType() {
        List<Object[]> results = cardRepository.countAccountsByCardType();
        Map<String, Long> cardCounts = new HashMap<>();
        for (Object[] result : results) {
            cardCounts.put((String) result[0], (Long) result[1]);
        }
        return cardCounts;
    }

        public List<CardModel> getCardsByCustomerNumber(String customerNumber) {
    return cardRepository.findByCustomerNumber(customerNumber);
}
}
