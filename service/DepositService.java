
package com.example.bank.service;

import com.example.bank.model.AccountModel;
import com.example.bank.model.DepositModel;
import com.example.bank.repository.AccountRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.bank.repository.DepositRepository;
import jakarta.transaction.Transactional;


@Service
public class DepositService {
    
    private final DepositRepository depositRepository;
    private final AccountRepository accountRepository;
    
    public DepositService(DepositRepository depositRepository, AccountRepository accountRepository) {
        this.depositRepository = depositRepository;
        this.accountRepository = accountRepository;
    }
    
    public DepositModel saveDeposit(DepositModel savingDepositModel){
        
        return depositRepository.save(savingDepositModel);
        
    }
    
       public List<DepositModel> getAllDeposit() {
        return depositRepository.findAll(); 
    }
       
        @Transactional
    public void createDeposit(DepositModel depositModel) {

            AccountModel account = accountRepository.findByAccountNumber(depositModel.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < depositModel.getAmount()) {
            throw new RuntimeException("Insufficient funds in the account");
        }

        // Deduct amount from account balance
        account.setBalance(account.getBalance() - depositModel.getAmount());
        accountRepository.save(account);

        // Save deposit
        depositRepository.save(depositModel);
    }

    public List<DepositModel> getDepositsByCustomerNumber(String customerNumber) {
    return depositRepository.findByCustomerNumber(customerNumber);
}


}
