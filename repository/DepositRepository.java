
package com.example.bank.repository;

import com.example.bank.model.DepositModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepositRepository extends JpaRepository<DepositModel, Integer> {
    
    @Query("SELECT d FROM DepositModel d WHERE d.customerNumber = :customerNumber")
List<DepositModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

}
