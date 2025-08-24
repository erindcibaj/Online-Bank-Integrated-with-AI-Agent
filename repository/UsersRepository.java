package com.example.bank.repository;

import com.example.bank.model.UsersModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {
    Optional<UsersModel> findByCustomerNumber(String customerNumber); 
    
    Optional <UsersModel> findByEmail(String email);
    
    Optional<UsersModel> findByResetToken(String token);
    
    Optional <UsersModel> findByPhone(String phone);

  
}
