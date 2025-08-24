
package com.example.bank.repository;

import com.example.bank.model.UnblockCardBusinessModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UnblockCardBusinessRepository extends JpaRepository<UnblockCardBusinessModel, Integer> {
    
    @Query("SELECT u.cardNumber FROM CardBusinessModel u WHERE u.cardNumber IS NOT NULL")
    List<String> findAllCardNumbers();
}
