
package com.example.bank.repository;

import com.example.bank.model.TransactionOwnAccountsModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionOwnAccountsRepository extends JpaRepository<TransactionOwnAccountsModel, Integer> {
    
    @Query("SELECT t FROM TransactionOwnAccountsModel t WHERE t.customerNumber = :customerNumber")
List<TransactionOwnAccountsModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

}
