
package com.example.bank.repository;

import com.example.bank.model.CardModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CardRepository extends JpaRepository<CardModel, String> {
    
    Optional<CardModel> findByCardNumber(String cardNumber); 
     
    @Query("SELECT cardType, COUNT(cardType) FROM CardModel GROUP BY cardType")
    List<Object[]> countAccountsByCardType();
    
        @Query("SELECT a.cardNumber FROM CardModel a WHERE a.customerNumber = :customerNumber")
        List<String> findCardNumbersByCustomerNumber(@Param("customerNumber") String customerNumber);
        
        @Query("SELECT c FROM CardModel c WHERE c.customerNumber = :customerNumber")
        List<CardModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

    
       
}
