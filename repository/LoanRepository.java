package com.example.bank.repository;

import com.example.bank.model.LoanModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<LoanModel, Integer> {
    
        Optional<LoanModel> findById(Integer id);
        
    @Query("SELECT loanType, COUNT(loanType) FROM LoanModel GROUP BY loanType")
    List<Object[]> countAccountsByLoanType();
    
    @Query("SELECT l FROM LoanModel l WHERE l.customerNumber = :customerNumber")
List<LoanModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

        
}
