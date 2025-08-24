
package com.example.bank.repository;

import com.example.bank.model.TransactionBusinessModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionBusinessRepository extends JpaRepository<TransactionBusinessModel, Integer> {
    
    @Query("SELECT SUM(amount) FROM TransactionBusinessModel")
    Integer sumTransactionBusiness();
    
    @Query("SELECT t FROM TransactionBusinessModel t WHERE t.customerNumber = :customerNumber")
    List<TransactionBusinessModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

    List<TransactionBusinessModel> findTop5ByCustomerNumberOrderByIdDesc(String customerNumber);

    List<TransactionBusinessModel> findByAccountNumberBeneficiaryIn(List<String> accountNumbers);

}
