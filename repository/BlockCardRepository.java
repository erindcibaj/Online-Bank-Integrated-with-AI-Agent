package com.example.bank.repository;

import com.example.bank.model.BlockCardModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BlockCardRepository extends JpaRepository<BlockCardModel, String> {
    
    @Query("SELECT u.cardNumber FROM CardModel u WHERE u.cardNumber IS NOT NULL")
    List<String> findAllCardNumbers();
    
}
