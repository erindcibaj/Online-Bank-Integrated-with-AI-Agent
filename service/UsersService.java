package com.example.bank.service;

import com.example.bank.model.UsersModel;
import com.example.bank.repository.UsersRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsersModel registerUser(UsersModel usersModel) {
        
        usersModel.setAdmin(0);
        // Encode password before saving
        usersModel.setPassword(passwordEncoder.encode(usersModel.getPassword()));
        usersModel.setConfirmPassword(passwordEncoder.encode(usersModel.getConfirmPassword()));
        return usersRepository.save(usersModel);
    }
    
    

    public UsersModel authenticate(String customerNumber, String rawPassword) {
        UsersModel usersModel = usersRepository.findByCustomerNumber(customerNumber).orElse(null);
        
        if (usersModel != null && passwordEncoder.matches(rawPassword, usersModel.getPassword())) { // Compare encrypted password
            return usersModel;
        }
        return null;
    }
    
    public boolean userExists(String customerNumber) {
        return usersRepository.findByCustomerNumber(customerNumber).isPresent();
    }

        public boolean emailExists(String email) {
             return usersRepository.findByEmail(email).isPresent();
    }
        
        public boolean phoneExists(String phone) {
             return usersRepository.findByPhone(phone).isPresent();
    }
        
        
        
    public String getUserFullName(String customerNumber) {
        return usersRepository.findByCustomerNumber(customerNumber)
                .map(user -> user.getName().toUpperCase() + " " + user.getLastname().toUpperCase())
                .orElse("User Not Found");
    }
    
    public List<UsersModel> getAllCustomers() {
        return usersRepository.findAll(); 
    }
            
    public UsersModel getCustomerById(Integer id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
    
    public void updateCustomer(UsersModel usersModel) {
        
    UsersModel existingUser = usersRepository.findById(usersModel.getId())
        .orElseThrow(() -> new RuntimeException("Customer not found"));


    // If the password was not changed in the form, keep existing password & confirmPassword
    if (usersModel.getPassword() == null || usersModel.getPassword().trim().isEmpty()) {
        usersModel.setPassword(existingUser.getPassword());
        usersModel.setConfirmPassword(existingUser.getConfirmPassword());
    }   
        usersRepository.save(usersModel);
    }
    
}
    

