
package com.example.bank.service;

import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.DepositBusinessModel;
import com.example.bank.repository.AccountNumberBusinessRepository;
import com.example.bank.repository.DepositBusinessRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DepositBusinessService {
    
    private final DepositBusinessRepository depositBusinessRepository;
    private final AccountNumberBusinessRepository accountNumberBusinessRepository;
    
    public DepositBusinessService(DepositBusinessRepository depositBusinessRepository, AccountNumberBusinessRepository accountNumberBusinessRepository) {
        this.depositBusinessRepository = depositBusinessRepository;
        this.accountNumberBusinessRepository = accountNumberBusinessRepository;
    }
    
    public DepositBusinessModel saveDepositBusiness(DepositBusinessModel depositBusinesstModel){
        
        return depositBusinessRepository.save(depositBusinesstModel);
        
    }
    
       public List<DepositBusinessModel> getAllDepositBusiness() {
        return depositBusinessRepository.findAll(); 
    }
       
               @Transactional
    public void createDepositBusiness(DepositBusinessModel depositBusinessModel) {

            AccountNumberBusinessModel accountBusiness = accountNumberBusinessRepository.findByAccountNumber(depositBusinessModel.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (accountBusiness.getBalance() < depositBusinessModel.getAmount()) {
            throw new RuntimeException("Insufficient funds in the account");
        }

        // Deduct amount from account balance
        accountBusiness.setBalance(accountBusiness.getBalance() - depositBusinessModel.getAmount());
        accountNumberBusinessRepository.save(accountBusiness);

        // Save deposit
       // depositModel.setAccount(account);
        depositBusinessRepository.save(depositBusinessModel);
    }
    
    public List<DepositBusinessModel> getDepositsByCustomerNumber(String customerNumber) {
    return depositBusinessRepository.findByCustomerNumber(customerNumber);
}
}
