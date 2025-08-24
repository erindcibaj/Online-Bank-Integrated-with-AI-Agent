
package com.example.bank.repository;

import com.example.bank.model.DepositBusinessModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepositBusinessRepository extends JpaRepository<DepositBusinessModel, Integer> {
    
    @Query("SELECT d FROM DepositBusinessModel d WHERE d.customerNumber = :customerNumber")
List<DepositBusinessModel> findByCustomerNumber(@Param("customerNumber") String customerNumber);

}
