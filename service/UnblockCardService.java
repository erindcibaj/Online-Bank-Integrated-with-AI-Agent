package com.example.bank.service;

import com.example.bank.model.UnblockCardModel;
import com.example.bank.repository.CardRepository;
import com.example.bank.repository.UnblockCardRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UnblockCardService {
    
    private final UnblockCardRepository unBlockCardRepository;
    private final CardRepository cardRepository;
    
    public UnblockCardService(UnblockCardRepository unBlockCardRepository, CardRepository cardRepository) {
        this.unBlockCardRepository = unBlockCardRepository;
        this.cardRepository = cardRepository;
    }
    
        public UnblockCardModel saveUnblockCard(UnblockCardModel unBlockCardModel){
        return unBlockCardRepository.save(unBlockCardModel);
    }
        
        public List<String> getAllCardNumbers() {
        return unBlockCardRepository.findAllCardNumbers();
    }
        
            public List<String> getCardNumbersByCustomerNumber(String customerNumber) {
        return cardRepository.findCardNumbersByCustomerNumber(customerNumber);
    }
}


