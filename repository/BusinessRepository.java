package com.example.bank.repository;

import com.example.bank.model.BusinessAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<BusinessAccount, Integer> {
        Optional<BusinessAccount> findByCustomerNumber(String customerNumber); 

        Optional<BusinessAccount> findByNipt(String nipt);
        
        Optional <BusinessAccount> findByEmail(String email);
        
        Optional<BusinessAccount> findByResetToken(String token);
        
        Optional<BusinessAccount> findByPhone(String phone);


}
