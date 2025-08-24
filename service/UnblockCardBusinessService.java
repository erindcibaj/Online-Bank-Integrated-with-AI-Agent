
package com.example.bank.service;

import com.example.bank.model.UnblockCardBusinessModel;
import com.example.bank.repository.CardBusinessRepository;
import com.example.bank.repository.UnblockCardBusinessRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UnblockCardBusinessService {
    
    private final UnblockCardBusinessRepository unBlockCardBusinessRepository;
    private final CardBusinessRepository cardBusinessRepository;
    
    public UnblockCardBusinessService(UnblockCardBusinessRepository unBlockCardBusinessRepository, CardBusinessRepository cardBusinessRepository) {
        this.unBlockCardBusinessRepository = unBlockCardBusinessRepository;
        this.cardBusinessRepository = cardBusinessRepository;
    }
    
        public UnblockCardBusinessModel saveUnblockCard(UnblockCardBusinessModel UnblockCardBusinessModel){
        
        return unBlockCardBusinessRepository.save(UnblockCardBusinessModel);
    }
        
        public List<String> getAllCardNumbers() {
        return unBlockCardBusinessRepository.findAllCardNumbers();
    }
        
         public List<String> getCardNumbersByCustomerNumber(String customerNumber) {
        return cardBusinessRepository.findCardNumbersByCustomerNumber(customerNumber);
    }

}
