package com.example.bank.repository;

import com.example.bank.model.AccountModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountRepository extends JpaRepository<AccountModel, Integer> {
    
    @Query("SELECT a.accountNumber FROM AccountModel a JOIN UsersModel u ON a.customerNumber = u.customerNumber") 
    List<String> findAllAccountNumbers();
    
    Optional<AccountModel> findByAccountNumber(String accountNumber);
    
   @Query("SELECT COUNT(*) FROM AccountModel")
    int countAllAccounts();
    
    @Query("SELECT a.accountNumber FROM AccountModel a WHERE a.customerNumber = :customerNumber")
    List<String> findAccountsByCustomerNumber(@Param("customerNumber") String customerNumber);

    @Query("SELECT a FROM AccountModel a WHERE a.customerNumber = :customerNumber ORDER BY a.balance DESC")
    List<AccountModel> findAccount(@Param("customerNumber") String customerNumber);
    
    boolean existsByCustomerNumber(String customerNumber);
    
    Optional<AccountModel> findByCustomerNumberAndAccountNumber(String customerNumber, String accountNumber);

}
