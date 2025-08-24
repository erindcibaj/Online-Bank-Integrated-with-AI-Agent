package com.example.bank.service;

import com.example.bank.model.AccountModel;
import com.example.bank.repository.AccountRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
   
    private final AccountRepository accountRepository;

    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public AccountModel saveAccount(AccountModel accountModel){
        
        return accountRepository.save(accountModel);
    }

    public String generateAccountNumber() {
        long number = 1000000000000L + (long) (Math.random() * 9000000000000L);
        return "AL" + number;
    }
    
        public List<AccountModel> getAllAccounts() {
        return accountRepository.findAll(); 
    }
    
    
    public List<String> getAllAccountNumbers() {
        return accountRepository.findAllAccountNumbers();
    }
    
    public List<String> getAccountsByCustomerNumber(String customerNumber) {
    return accountRepository.findAccountsByCustomerNumber(customerNumber);
}
    
        
    public Map<String, Integer> getAccountStats() {
        Map<String, Integer> data = new HashMap<>();
        data.put("Personal Accounts", accountRepository.countAllAccounts());
        return data;
    }
    
       public AccountModel getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void updateAccount(AccountModel accountModel) {
        accountRepository.save(accountModel);
    }
            public List<AccountModel> getAccount(String customerNumber) {
    return accountRepository.findAccount(customerNumber);
}
    
            public boolean customerExists(String customerNumber) {
        return accountRepository.existsByCustomerNumber(customerNumber);
    }

public Map<String, Object> getBalanceForAccount(String customerNumber, String accountNumber) {
    AccountModel account = accountRepository.findByCustomerNumberAndAccountNumber(customerNumber, accountNumber)
            .orElseThrow(() -> new RuntimeException("Account not found or not linked to your profile."));

    Map<String, Object> response = new HashMap<>();
    response.put("balance", account.getBalance());
    response.put("currency", account.getCurrency());

    return response;
}
 
public String getFullNameByAccountNumber(String accountNumber) {
    Optional<AccountModel> accountOptional = accountRepository.findByAccountNumber(accountNumber);
    
    if (accountOptional.isPresent()) {
        AccountModel account = accountOptional.get();
        return account.getName() + " " + account.getLastName(); // or getFirstName() + getLastName()
    } else {
        throw new RuntimeException("Account not found");
    }
}

    public String getCustomerNumberByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(AccountModel::getCustomerNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
    }
}



