
package com.example.bank.repository;

import com.example.bank.model.CardApplicationBusinessModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardApplicationBusinessRepository extends JpaRepository<CardApplicationBusinessModel, Integer> {
    
}
