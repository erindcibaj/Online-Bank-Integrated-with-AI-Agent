
package com.example.bank.repository;

import com.example.bank.model.AccountNumberBusinessModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountNumberBusinessRepository extends JpaRepository<AccountNumberBusinessModel, Integer> {
    
    @Query("SELECT u.accountNumber FROM AccountNumberBusinessModel u WHERE u.accountNumber IS NOT NULL")
    List<String> findAllAccountNumbersBusiness();
    
    Optional<AccountNumberBusinessModel> findByAccountNumber(String accountNumber);

    @Query("SELECT COUNT(*) FROM AccountNumberBusinessModel")
    int countAllAccounts();
   
    @Query("SELECT a.accountNumber FROM AccountNumberBusinessModel a WHERE a.customerNumber = :customerNumber")
    List<String> findAccountsByCustomerNumber(@Param("customerNumber") String customerNumber);
    
        @Query("SELECT a FROM AccountNumberBusinessModel a WHERE a.customerNumber = :customerNumber ORDER BY a.balance DESC")
    List<AccountNumberBusinessModel> findAccount(@Param("customerNumber") String customerNumber);
    
            boolean existsByCustomerNumber(String customerNumber);

    Optional<AccountNumberBusinessModel> findByCustomerNumberAndAccountNumber(String customerNumber, String accountNumber);


}
