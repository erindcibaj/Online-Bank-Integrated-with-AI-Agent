
package com.example.bank.repository;

import com.example.bank.model.TransactionModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<TransactionModel, Integer> {
    
   
    @Query("SELECT SUM(amount) FROM TransactionModel")
    Integer sumTransactionPersonal();
    
    @Query("SELECT t FROM TransactionModel t WHERE t.customerNumber = :customerNumber")
    List<TransactionModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

    List<TransactionModel> findTop5ByCustomerNumberOrderByIdDesc(String customerNumber);

    List<TransactionModel> findByAccountNumberBeneficiaryIn(List<String> accountNumbers);

}
