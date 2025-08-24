
package com.example.bank.repository;

import com.example.bank.model.LoanBusinessModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LoanBusinessRepository extends JpaRepository<LoanBusinessModel, Integer> {
    
            Optional<LoanBusinessModel> findById(Integer id);

    @Query("SELECT loanType, COUNT(loanType) FROM LoanBusinessModel GROUP BY loanType")
    List<Object[]> countAccountsByLoanType();
    
    @Query("SELECT l FROM LoanBusinessModel l WHERE l.customerNumber = :customerNumber")
List<LoanBusinessModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

}
