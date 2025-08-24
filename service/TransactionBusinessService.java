
package com.example.bank.service;

import com.example.bank.model.AccountModel;
import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.TransactionBusinessModel;
import com.example.bank.repository.AccountNumberBusinessRepository;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionBusinessRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TransactionBusinessService {
    
    private final TransactionBusinessRepository transactionBusinessRepository;
    private final AccountNumberBusinessRepository accountNumberBusinessRepository;
    private final AccountRepository accountRepository;
    
    public TransactionBusinessService(TransactionBusinessRepository transactionBusinessRepository, AccountNumberBusinessRepository accountNumberBusinessRepository, AccountRepository accountRepository) {
        this.transactionBusinessRepository = transactionBusinessRepository;
        this.accountNumberBusinessRepository = accountNumberBusinessRepository;
        this.accountRepository = accountRepository;
    }
    
    public TransactionBusinessModel saveTransaction(TransactionBusinessModel transactionBusinessModel){
        
        return transactionBusinessRepository.save(transactionBusinessModel);
        
    }
    
       public List<TransactionBusinessModel> getAllTransactionBusiness() {
        return transactionBusinessRepository.findAll(); 
    }
       
@Transactional
public void transferMoney(TransactionBusinessModel transactionModel) {
    // Find sender (either Business or Personal)
    AccountNumberBusinessModel senderBusiness = accountNumberBusinessRepository.findByAccountNumber(transactionModel.getAccountNumber()).orElse(null);
    AccountModel senderPersonal = accountRepository.findByAccountNumber(transactionModel.getAccountNumber()).orElse(null);

    // Find receiver (either Business or Personal)
    AccountNumberBusinessModel receiverBusiness = accountNumberBusinessRepository.findByAccountNumber(transactionModel.getAccountNumberBeneficiary()).orElse(null);
    AccountModel receiverPersonal = accountRepository.findByAccountNumber(transactionModel.getAccountNumberBeneficiary()).orElse(null);

    // Validate sender existence
    if (senderBusiness == null && senderPersonal == null) {
        throw new RuntimeException("Sender account not found");
    }

    // Validate receiver existence
    if (receiverBusiness == null && receiverPersonal == null) {
        throw new RuntimeException("Beneficiary account not found");
    }

    // Determine sender and check balance
    if (senderBusiness != null) {
        if (senderBusiness.getBalance() < transactionModel.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        senderBusiness.setBalance(senderBusiness.getBalance() - transactionModel.getAmount());
        accountNumberBusinessRepository.save(senderBusiness);
    } else {
        if (senderPersonal.getBalance() < transactionModel.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        senderPersonal.setBalance(senderPersonal.getBalance() - transactionModel.getAmount());
        accountRepository.save(senderPersonal);
    }

    // Determine receiver and credit balance
    if (receiverBusiness != null) {
        receiverBusiness.setBalance(receiverBusiness.getBalance() + transactionModel.getAmount());
        accountNumberBusinessRepository.save(receiverBusiness);
    } else {
        receiverPersonal.setBalance(receiverPersonal.getBalance() + transactionModel.getAmount());
        accountRepository.save(receiverPersonal);
    }

    // Save transaction record
    transactionBusinessRepository.save(transactionModel);
}
       
        public Map<String, Integer> getSumTransactionBusiness() {
        Map<String, Integer> data = new HashMap<>();
        data.put("Business Accounts", transactionBusinessRepository.sumTransactionBusiness());
        return data;
    }
        
        public List<TransactionBusinessModel> getTransactionsByCustomerNumber(String customerNumber) {
    return transactionBusinessRepository.findByCustomerNumber(customerNumber);
}

        public List<TransactionBusinessModel> get5TransactionsByCustomerNumber(String customerNumber) {
    return transactionBusinessRepository.findTop5ByCustomerNumberOrderByIdDesc(customerNumber);
}
        
    public List<TransactionBusinessModel> findByBeneficiaryAccounts(List<String> beneficiaryAccountNumbers) {
    return transactionBusinessRepository.findByAccountNumberBeneficiaryIn(beneficiaryAccountNumbers);
}

}
