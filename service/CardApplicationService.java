package com.example.bank.service;

import com.example.bank.model.CardApplicationModel;
import com.example.bank.repository.CardApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class CardApplicationService {
   
    private final CardApplicationRepository cardApplicationRepository;
    
    public CardApplicationService(CardApplicationRepository cardApplicationRepository) {
        this.cardApplicationRepository = cardApplicationRepository;
    }
    
    public CardApplicationModel saveApplication(CardApplicationModel cardApplicationModel){
        
        return cardApplicationRepository.save(cardApplicationModel);
    }
}
