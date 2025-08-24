package com.example.bank.service;

import com.example.bank.model.BlockCardModel;
import com.example.bank.repository.BlockCardRepository;
import com.example.bank.repository.CardRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BlockCardService {
    
    private final BlockCardRepository blockCardRepository;
    private final CardRepository cardRepository;
    
    public BlockCardService(BlockCardRepository blockCardRepository, CardRepository cardRepository) {
        this.blockCardRepository = blockCardRepository;
        this.cardRepository = cardRepository;
    }
    
        public BlockCardModel saveBlockCard(BlockCardModel blockCardModel){
        
        return blockCardRepository.save(blockCardModel);
    }
        
        public List<String> getAllCardNumbers() {
        return blockCardRepository.findAllCardNumbers();
    }
            // Fetch card numbers associated with the customer number
    public List<String> getCardNumbersByCustomerNumber(String customerNumber) {
        return cardRepository.findCardNumbersByCustomerNumber(customerNumber);
    }

}
