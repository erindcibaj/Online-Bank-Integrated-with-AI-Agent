
package com.example.bank.service;

import com.example.bank.model.BlockCardBusinessModel;
import com.example.bank.repository.BlockCardBusinessRepository;
import com.example.bank.repository.CardBusinessRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BlockCardBusinessService {
    
     private final BlockCardBusinessRepository blockCardBusinessRepository;
     private final CardBusinessRepository cardBusinessRepository;
    
    public BlockCardBusinessService(BlockCardBusinessRepository blockCardBusinessRepository, CardBusinessRepository cardBusinessRepository) {
        this.blockCardBusinessRepository = blockCardBusinessRepository;
        this.cardBusinessRepository = cardBusinessRepository;
    }
    
        public BlockCardBusinessModel saveBlockCard(BlockCardBusinessModel blockCardBusinessModel){
        
        return blockCardBusinessRepository.save(blockCardBusinessModel);
    }
        
        public List<String> getAllCardNumbers() {
        return blockCardBusinessRepository.findAllCardNumbers();
    }
        
            // Fetch card numbers associated with the customer number
    public List<String> getCardNumbersByCustomerNumber(String customerNumber) {
        return cardBusinessRepository.findCardNumbersByCustomerNumber(customerNumber);
    }
}
