
package com.example.bank.service;

import com.example.bank.model.AccountModel;
import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.TransactionModel;
import com.example.bank.repository.AccountNumberBusinessRepository;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountNumberBusinessRepository accountNumberBusinessRepository;
    
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,AccountNumberBusinessRepository accountNumberBusinessRepository ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountNumberBusinessRepository = accountNumberBusinessRepository;
    }
    
    public TransactionModel saveTransaction(TransactionModel transactionModel){
        
        return transactionRepository.save(transactionModel);
        
    }
    
       public List<TransactionModel> getAllTransaction() {
        return transactionRepository.findAll(); 
    }
       
    @Transactional
    public void transferMoney(TransactionModel transactionModel) {
        // Find sender (either Personal or Business)
        AccountModel senderPersonal = accountRepository.findByAccountNumber(transactionModel.getAccountNumber()).orElse(null);
        AccountNumberBusinessModel senderBusiness = accountNumberBusinessRepository.findByAccountNumber(transactionModel.getAccountNumber()).orElse(null);

        // Find receiver (either Personal or Business)
        AccountModel receiverPersonal = accountRepository.findByAccountNumber(transactionModel.getAccountNumberBeneficiary()).orElse(null);
        AccountNumberBusinessModel receiverBusiness = accountNumberBusinessRepository.findByAccountNumber(transactionModel.getAccountNumberBeneficiary()).orElse(null);

        // Validate sender existence
        if (senderPersonal == null && senderBusiness == null) {
            throw new RuntimeException("Sender account not found");
        }

        // Validate receiver existence
        if (receiverPersonal == null && receiverBusiness == null) {
            throw new RuntimeException("Beneficiary account not found");
        }

        // Determine sender's balance and deduct money
        if (senderPersonal != null) {
            if (senderPersonal.getBalance() < transactionModel.getAmount()) {
                throw new RuntimeException("Insufficient funds");
            }
            senderPersonal.setBalance(senderPersonal.getBalance() - transactionModel.getAmount());
            accountRepository.save(senderPersonal);
        } else {
            if (senderBusiness.getBalance() < transactionModel.getAmount()) {
                throw new RuntimeException("Insufficient funds");
            }
            senderBusiness.setBalance(senderBusiness.getBalance() - transactionModel.getAmount());
            accountNumberBusinessRepository.save(senderBusiness);
        }

        // Determine receiver and add money
        if (receiverPersonal != null) {
            receiverPersonal.setBalance(receiverPersonal.getBalance() + transactionModel.getAmount());
            accountRepository.save(receiverPersonal);
        } else {
            receiverBusiness.setBalance(receiverBusiness.getBalance() + transactionModel.getAmount());
            accountNumberBusinessRepository.save(receiverBusiness);
        }

        // Save transaction record
        transactionRepository.save(transactionModel);
    }
       
        public Map<String, Integer> getSumTransactionPersonal() {
        Map<String, Integer> data = new HashMap<>();
        data.put("Personal Accounts", transactionRepository.sumTransactionPersonal());
        return data;
    }
        
    public List<TransactionModel> getTransactionsByCustomerNumber(String customerNumber) {
    return transactionRepository.findByCustomerNumber(customerNumber);
}

    public List<TransactionModel> get5TransactionsByCustomerNumber(String customerNumber) {
    return transactionRepository.findTop5ByCustomerNumberOrderByIdDesc(customerNumber);
}
    
    public List<TransactionModel> findByBeneficiaryAccounts(List<String> beneficiaryAccountNumbers) {
    return transactionRepository.findByAccountNumberBeneficiaryIn(beneficiaryAccountNumbers);
}

    
}
