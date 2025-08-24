
package com.example.bank.repository;

import com.example.bank.model.TransactionOwnAccountsBusinessModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionOwnAccountsBusinessRepository extends JpaRepository<TransactionOwnAccountsBusinessModel, Integer> {
    
    @Query("SELECT t FROM TransactionOwnAccountsBusinessModel t WHERE t.customerNumber = :customerNumber")
List<TransactionOwnAccountsBusinessModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

}
