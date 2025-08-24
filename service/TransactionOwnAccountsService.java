
package com.example.bank.service;

import com.example.bank.model.AccountModel;
import com.example.bank.model.TransactionOwnAccountsModel;
import com.example.bank.repository.AccountRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.bank.repository.TransactionOwnAccountsRepository;
import jakarta.transaction.Transactional;

@Service
public class TransactionOwnAccountsService {
    
    private final TransactionOwnAccountsRepository transactionOwnAccountsRepository;
    private final AccountRepository accountRepository;
    
    public TransactionOwnAccountsService(TransactionOwnAccountsRepository transactionOwnAccountsRepository, AccountRepository accountRepository) {
        this.transactionOwnAccountsRepository = transactionOwnAccountsRepository;
        this.accountRepository = accountRepository;
    }
    
    public TransactionOwnAccountsModel saveTransaction(TransactionOwnAccountsModel transactionOwnAccountsModel){
        
        return transactionOwnAccountsRepository.save(transactionOwnAccountsModel);
        
    }
    
       public List<TransactionOwnAccountsModel> getAllTransaction() {
        return transactionOwnAccountsRepository.findAll(); 
    }
       
       @Transactional
       public void transferMoney(TransactionOwnAccountsModel transactionOwnAccountsModel) {
        AccountModel sender = accountRepository.findByAccountNumber(transactionOwnAccountsModel.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        AccountModel receiver = accountRepository.findByAccountNumber(transactionOwnAccountsModel.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Beneficiary account not found"));

        if (sender.getBalance() < transactionOwnAccountsModel.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance() - transactionOwnAccountsModel.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionOwnAccountsModel.getAmount());

        accountRepository.save(sender);
        accountRepository.save(receiver);
        transactionOwnAccountsRepository.save(transactionOwnAccountsModel);
    }
       
       public List<TransactionOwnAccountsModel> getTransactionsByCustomerNumber(String customerNumber) {
    return transactionOwnAccountsRepository.findByCustomerNumber(customerNumber);
}


}
