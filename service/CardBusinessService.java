
package com.example.bank.service;

import com.example.bank.model.CardBusinessModel;
import com.example.bank.repository.CardBusinessRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CardBusinessService {
    
    private final CardBusinessRepository cardBusinessRepository;

    public CardBusinessService(CardBusinessRepository cardBusinessRepository) {
        this.cardBusinessRepository = cardBusinessRepository;
    }
    
        public CardBusinessModel saveCard(CardBusinessModel cardBusinessModel){
        
        return cardBusinessRepository.save(cardBusinessModel);
        
    }
        
          public List<CardBusinessModel> getAllCards() {
        return cardBusinessRepository.findAll(); // Fetch all cards
    }
          
          public boolean cardExists(String cardNumber){
              return cardBusinessRepository.findByCardNumber(cardNumber).isPresent();
          }
          
        public Map<String, Long> countAccountsByCardType() {
        List<Object[]> results = cardBusinessRepository.countAccountsByCardType();
        Map<String, Long> cardCounts = new HashMap<>();
        for (Object[] result : results) {
            cardCounts.put((String) result[0], (Long) result[1]);
        }
        return cardCounts;
    }
          
        public List<CardBusinessModel> getCardsByCustomerNumber(String customerNumber) {
    return cardBusinessRepository.findByCustomerNumber(customerNumber);
}

}
