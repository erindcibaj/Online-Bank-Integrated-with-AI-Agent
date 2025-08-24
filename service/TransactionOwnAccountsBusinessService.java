
package com.example.bank.service;

import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.TransactionOwnAccountsBusinessModel;
import com.example.bank.repository.AccountNumberBusinessRepository;
import com.example.bank.repository.TransactionOwnAccountsBusinessRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionOwnAccountsBusinessService {
    
    private final TransactionOwnAccountsBusinessRepository transactionOwnAccountsBusinessRepository;
    private final AccountNumberBusinessRepository accountNumberBusinessRepository;
    
    public TransactionOwnAccountsBusinessService(TransactionOwnAccountsBusinessRepository transactionOwnAccountsBusinessRepository, AccountNumberBusinessRepository accountNumberBusinessRepository) {
        this.transactionOwnAccountsBusinessRepository = transactionOwnAccountsBusinessRepository;
        this.accountNumberBusinessRepository = accountNumberBusinessRepository;
    }
    
    public TransactionOwnAccountsBusinessModel saveTransaction(TransactionOwnAccountsBusinessModel transactionOwnAccountsBusinessModel){
        
        return transactionOwnAccountsBusinessRepository.save(transactionOwnAccountsBusinessModel);
        
    }
    
           public List<TransactionOwnAccountsBusinessModel> getAllTransaction() {
        return transactionOwnAccountsBusinessRepository.findAll(); 
    }
    
        @Transactional
       public void transferMoney(TransactionOwnAccountsBusinessModel transactionOwnAccountsBusinessModel) {
        AccountNumberBusinessModel sender = accountNumberBusinessRepository.findByAccountNumber(transactionOwnAccountsBusinessModel.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        AccountNumberBusinessModel receiver = accountNumberBusinessRepository.findByAccountNumber(transactionOwnAccountsBusinessModel.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Beneficiary account not found"));

        if (sender.getBalance() < transactionOwnAccountsBusinessModel.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance() - transactionOwnAccountsBusinessModel.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionOwnAccountsBusinessModel.getAmount());

        accountNumberBusinessRepository.save(sender);
        accountNumberBusinessRepository.save(receiver);
        transactionOwnAccountsBusinessRepository.save(transactionOwnAccountsBusinessModel);
    }
       public List<TransactionOwnAccountsBusinessModel> getTransactionsByCustomerNumber(String customerNumber) {
    return transactionOwnAccountsBusinessRepository.findByCustomerNumber(customerNumber);
}

}
