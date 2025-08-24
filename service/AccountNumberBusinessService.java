
package com.example.bank.service;

import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.repository.AccountNumberBusinessRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountNumberBusinessService {
   
    private final AccountNumberBusinessRepository accountNumberBusinessRepository;

    
    public AccountNumberBusinessService(AccountNumberBusinessRepository accountNumberBusinessRepository) {
        this.accountNumberBusinessRepository = accountNumberBusinessRepository;
    }
    
    public AccountNumberBusinessModel saveAccount(AccountNumberBusinessModel accountNumberBusinessModel){
        
        return accountNumberBusinessRepository.save(accountNumberBusinessModel);
    }

    public String generateAccountNumber() {
        long number = 1000000000000L + (long) (Math.random() * 9000000000000L);
        return "AL" + number;
    }
    
        public List<AccountNumberBusinessModel> getAllAccounts() {
        return accountNumberBusinessRepository.findAll(); 
    }
        
     public List<String> getAllAccountNumbersBusiness() {
        return accountNumberBusinessRepository.findAllAccountNumbersBusiness();
    }
     
        public Map<String, Integer> getAccountStats() {
        Map<String, Integer> data = new HashMap<>();
        data.put("Business Accounts", accountNumberBusinessRepository.countAllAccounts());
        return data;
    }
        
            public List<String> getAccountsByCustomerNumber(String customerNumber) {
    return accountNumberBusinessRepository.findAccountsByCustomerNumber(customerNumber);
}
            
          public AccountNumberBusinessModel getAccountById(Integer id) {
        return accountNumberBusinessRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void updateAccount(AccountNumberBusinessModel accountNumberBusinessModel) {
        accountNumberBusinessRepository.save(accountNumberBusinessModel);
    }
    
        public List<AccountNumberBusinessModel> getAccount(String customerNumber) {
             return accountNumberBusinessRepository.findAccount(customerNumber);
    }
        
            public boolean customerExists(String customerNumber) {
        return accountNumberBusinessRepository.existsByCustomerNumber(customerNumber);
    }
            
            public Map<String, Object> getBalanceForAccount(String customerNumber, String accountNumber) {
    AccountNumberBusinessModel account = accountNumberBusinessRepository.findByCustomerNumberAndAccountNumber(customerNumber, accountNumber)
            .orElseThrow(() -> new RuntimeException("Account not found or not linked to your profile."));

    Map<String, Object> response = new HashMap<>();
    response.put("balance", account.getBalance());
    response.put("currency", account.getCurrency());

    return response;
}
            
        public String getFullNameByAccountNumber(String accountNumber) {
    Optional<AccountNumberBusinessModel> accountOptional = accountNumberBusinessRepository.findByAccountNumber(accountNumber);
    
    if (accountOptional.isPresent()) {
        AccountNumberBusinessModel account = accountOptional.get();
        return account.getName() + " " + account.getLastName(); // or getFirstName() + getLastName()
    } else {
        throw new RuntimeException("Account not found");
    }
}
        
            public String getCustomerNumberByAccountNumber(String accountNumber) {
        return accountNumberBusinessRepository.findByAccountNumber(accountNumber)
                .map(AccountNumberBusinessModel::getCustomerNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
    }
}
