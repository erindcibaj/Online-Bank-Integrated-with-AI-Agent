package com.example.bank.controller;

import com.example.bank.model.TransactionModel;
import com.example.bank.model.TransactionOwnAccountsModel;
import com.example.bank.service.TransactionOwnAccountsService;
import com.example.bank.service.TransactionService;
import com.example.bank.model.AccountModel;
import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.TransactionBusinessModel;
import com.example.bank.model.TransactionOwnAccountsBusinessModel;
import com.example.bank.repository.AccountNumberBusinessRepository;
import com.example.bank.repository.AccountRepository;
import com.example.bank.service.TransactionBusinessService;
import com.example.bank.service.TransactionOwnAccountsBusinessService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionControllerChatbot {

    private final TransactionService transactionService;
    private final TransactionOwnAccountsService transactionOwnAccountsService;
    private final TransactionBusinessService transactionBusinessService;
    private final TransactionOwnAccountsBusinessService transactionOwnAccountsBusinessService;
    private final AccountRepository accountRepository;
    private final AccountNumberBusinessRepository accountNumberBusinessRepository;

    public TransactionControllerChatbot(TransactionService transactionService,
                                        TransactionOwnAccountsService transactionOwnAccountsService,
                                        TransactionBusinessService transactionBusinessService,
                                        TransactionOwnAccountsBusinessService transactionOwnAccountsBusinessService,
                                        AccountRepository accountRepository,
                                        AccountNumberBusinessRepository accountNumberBusinessRepository) {
        this.transactionService = transactionService;
        this.transactionOwnAccountsService = transactionOwnAccountsService;
        this.transactionBusinessService = transactionBusinessService;
        this.transactionOwnAccountsBusinessService = transactionOwnAccountsBusinessService;
        this.accountRepository = accountRepository;
        this.accountNumberBusinessRepository = accountNumberBusinessRepository;
    }

   @PostMapping("/transfer")
public ResponseEntity<String> transferMoneyByChatbot(@RequestBody TransactionModel transactionModel, HttpSession session) {
    String customerNumber = (String) session.getAttribute("customerNumber");
    transactionModel.setCustomerNumber(customerNumber);

    if (transactionModel.getAccountNumber().equals(transactionModel.getAccountNumberBeneficiary())) {
        return ResponseEntity.badRequest().body("Sender and receiver accounts cannot be the same.");
    }

    if (transactionModel.getAmount() == 0) {
        return ResponseEntity.badRequest().body("Amount must be greater than zero.");
    }
    
    if (transactionModel.getAmount() < 0) {
        return ResponseEntity.badRequest().body("Amount can't be negative.");
    }

    try {
        AccountModel senderPersonal = accountRepository.findByAccountNumber(transactionModel.getAccountNumber()).orElse(null);
        AccountNumberBusinessModel senderBusiness = accountNumberBusinessRepository.findByAccountNumber(transactionModel.getAccountNumber()).orElse(null);

    if (senderPersonal == null && senderBusiness == null) {
        throw new RuntimeException("Sender account not found.");
    }

    // Check if sender account belongs to the logged-in customer
    if (senderPersonal != null && !senderPersonal.getCustomerNumber().equals(customerNumber)) {
        return ResponseEntity.status(403).body("You are not authorized to transfer from this account.");
    }

    if (senderBusiness != null && !senderBusiness.getCustomerNumber().equals(customerNumber)) {
        return ResponseEntity.status(403).body("You are not authorized to transfer from this account.");
    }


        AccountModel receiverPersonal = accountRepository.findByAccountNumber(transactionModel.getAccountNumberBeneficiary()).orElse(null);
        AccountNumberBusinessModel receiverBusiness = accountNumberBusinessRepository.findByAccountNumber(transactionModel.getAccountNumberBeneficiary()).orElse(null);

        if (receiverPersonal == null && receiverBusiness == null) {
            throw new RuntimeException("Receiver account not found");
        }

        boolean senderIsPersonal = (senderPersonal != null);
        boolean receiverIsPersonal = (receiverPersonal != null);

        boolean sameCustomer =
                (senderIsPersonal && receiverIsPersonal && senderPersonal.getCustomerNumber().equals(receiverPersonal.getCustomerNumber())) ||
                (!senderIsPersonal && !receiverIsPersonal && senderBusiness.getCustomerNumber().equals(receiverBusiness.getCustomerNumber()));

        boolean nameProvided = transactionModel.getName() != null && !transactionModel.getName().trim().isEmpty();
        boolean descriptionProvided = transactionModel.getDescription() != null && !transactionModel.getDescription().trim().isEmpty();

        //  Modified and Corrected Validation Logic
        if (sameCustomer) { // Own account transfer
            if (nameProvided && descriptionProvided) {
        return ResponseEntity.badRequest().body("Transfers to your own account are not allowed when name is provided.");
        }
            if (!descriptionProvided) {
                return ResponseEntity.badRequest().body("Description is required.");
            }
        } else { // External transfer
            if (nameProvided && !descriptionProvided) {
                return ResponseEntity.badRequest().body("Description is required.");
            }
            if (!nameProvided || !descriptionProvided) {
                return ResponseEntity.badRequest().body("Name and description are required.");
            }
        }

        //Proceed with the transfer logic
        if (sameCustomer && senderIsPersonal) {
            TransactionOwnAccountsModel ownTransaction = new TransactionOwnAccountsModel();
            ownTransaction.setFromAccountNumber(transactionModel.getAccountNumber());
            ownTransaction.setToAccountNumber(transactionModel.getAccountNumberBeneficiary());
            ownTransaction.setAmount(transactionModel.getAmount());
            ownTransaction.setCurrency(transactionModel.getCurrency());
            ownTransaction.setDescription(transactionModel.getDescription());
            ownTransaction.setCustomerNumber(customerNumber);
            
            // Automatically set the sender's full name for own account transfer
            String fullName = senderPersonal.getName() + " " + senderPersonal.getLastName();
            ownTransaction.setName(fullName);

            transactionOwnAccountsService.transferMoney(ownTransaction);
        } else if (sameCustomer) {
            TransactionOwnAccountsBusinessModel businessOwnTransaction = new TransactionOwnAccountsBusinessModel();
            businessOwnTransaction.setFromAccountNumber(transactionModel.getAccountNumber());
            businessOwnTransaction.setToAccountNumber(transactionModel.getAccountNumberBeneficiary());
            businessOwnTransaction.setAmount(transactionModel.getAmount());
            businessOwnTransaction.setCurrency(transactionModel.getCurrency());
            businessOwnTransaction.setDescription(transactionModel.getDescription());
            businessOwnTransaction.setCustomerNumber(customerNumber);
            
            // Automatically set the sender's full name for own account transfer
            String fullName = senderPersonal.getName() + " " + senderPersonal.getLastName();
            businessOwnTransaction.setName(fullName);


            transactionOwnAccountsBusinessService.transferMoney(businessOwnTransaction);
        } else {
            if (senderIsPersonal) {
                transactionService.transferMoney(transactionModel);
            } else {
                TransactionBusinessModel crossTransaction = new TransactionBusinessModel();
                crossTransaction.setAccountNumber(transactionModel.getAccountNumber());
                crossTransaction.setAccountNumberBeneficiary(transactionModel.getAccountNumberBeneficiary());
                crossTransaction.setName(transactionModel.getName());
                crossTransaction.setAmount(transactionModel.getAmount());
                crossTransaction.setCurrency(transactionModel.getCurrency());
                crossTransaction.setDescription(transactionModel.getDescription());
                crossTransaction.setCustomerNumber(customerNumber);

                transactionBusinessService.transferMoney(crossTransaction);
            }
        }

        return ResponseEntity.ok("Transfer successful.");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body( e.getMessage());
    }
}

}
