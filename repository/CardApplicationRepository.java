
package com.example.bank.repository;

import com.example.bank.model.CardApplicationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardApplicationRepository extends JpaRepository<CardApplicationModel, String> {
    
}
