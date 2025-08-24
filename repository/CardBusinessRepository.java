
package com.example.bank.repository;

import com.example.bank.model.CardBusinessModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CardBusinessRepository extends JpaRepository<CardBusinessModel, Integer> {
    
        Optional<CardBusinessModel> findByCardNumber(String cardNumber); 

        @Query("SELECT cardType, COUNT(cardType) FROM CardBusinessModel GROUP BY cardType")
         List<Object[]> countAccountsByCardType();
             
        @Query("SELECT a.cardNumber FROM CardBusinessModel a WHERE a.customerNumber = :customerNumber")
        List<String> findCardNumbersByCustomerNumber(@Param("customerNumber") String customerNumber);
        
        @Query("SELECT c FROM CardBusinessModel c WHERE c.customerNumber = :customerNumber")
         List<CardBusinessModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

}
