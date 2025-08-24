package com.example.bank.service;

import com.example.bank.model.BusinessAccount;
import com.example.bank.repository.BusinessRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    
    private final BusinessRepository businessRepository;
    
    private final PasswordEncoder passwordEncoder;

    public BusinessService(BusinessRepository businessRepository, PasswordEncoder passwordEncoder) {
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user with input validation
    public BusinessAccount registerBusiness(BusinessAccount businessAccount) {
        // Encode password before saving
        businessAccount.setPassword(passwordEncoder.encode(businessAccount.getPassword()));
        businessAccount.setConfirmPassword(passwordEncoder.encode(businessAccount.getConfirmPassword()));
        return businessRepository.save(businessAccount);
    }

    // Authenticate user with hashed password comparison

    public BusinessAccount authenticate(String customerNumber, String rawPassword) {
        BusinessAccount businessAccount = businessRepository.findByCustomerNumber(customerNumber).orElse(null);
        
        if (businessAccount != null && passwordEncoder.matches(rawPassword, businessAccount.getPassword())) { // Compare encrypted password
            return businessAccount;
        }
        return null;
    }
    
      public boolean userExists(String customerNumber) {
    return businessRepository.findByCustomerNumber(customerNumber).isPresent();
    }
      
    public boolean niptExists(String nipt) {
    return businessRepository.findByNipt(nipt).isPresent();
    }
        
    public boolean emailExists(String email) {
     return businessRepository.findByEmail(email).isPresent();
    }
    
        public boolean phoneExists(String phone) {
     return businessRepository.findByPhone(phone).isPresent();
    }
    
    public List<BusinessAccount> getAllCustomers() {
        return businessRepository.findAll(); 
    }
        
    public BusinessAccount getCustomerById(Integer id) {
        return businessRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
    
public void updateCustomer(BusinessAccount businessAccount) {
    BusinessAccount existingAccount = businessRepository.findById(businessAccount.getId())
        .orElseThrow(() -> new RuntimeException("Customer not found"));

    // Check if password field was left empty (i.e., not updating it)
    if (businessAccount.getPassword() == null || businessAccount.getPassword().trim().isEmpty()) {
        businessAccount.setPassword(existingAccount.getPassword());
        businessAccount.setConfirmPassword(existingAccount.getConfirmPassword());
    }

    businessRepository.save(businessAccount);
}
    
}

    


