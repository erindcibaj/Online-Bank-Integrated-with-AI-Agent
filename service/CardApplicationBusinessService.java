
package com.example.bank.service;

import com.example.bank.model.CardApplicationBusinessModel;
import com.example.bank.repository.CardApplicationBusinessRepository;
import org.springframework.stereotype.Service;

@Service
public class CardApplicationBusinessService {
    
    private final CardApplicationBusinessRepository cardApplicationBusinessRepository;
    
    public CardApplicationBusinessService(CardApplicationBusinessRepository cardApplicationBusinessRepository) {
        this.cardApplicationBusinessRepository = cardApplicationBusinessRepository;
    }
    
    public CardApplicationBusinessModel saveApplication(CardApplicationBusinessModel cardApplicationBusinessModel){
        
        return cardApplicationBusinessRepository.save(cardApplicationBusinessModel);
    }
}
