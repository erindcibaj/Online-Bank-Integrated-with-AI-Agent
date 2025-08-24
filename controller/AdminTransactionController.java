
package com.example.bank.controller;

import com.example.bank.model.TransactionBusinessModel;
import com.example.bank.model.TransactionModel;
import com.example.bank.model.TransactionOwnAccountsBusinessModel;
import com.example.bank.model.TransactionOwnAccountsModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionBusinessService;
import com.example.bank.service.TransactionOwnAccountsBusinessService;
import com.example.bank.service.TransactionOwnAccountsService;
import com.example.bank.service.TransactionService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminTransactionController {
    
    private final TransactionService transactionService;
    private final TransactionBusinessService transactionBusinessService;
    private final TransactionOwnAccountsService transactionOwnAccountService;
    private final TransactionOwnAccountsBusinessService transactionOwnAccountsBusinessService;
    private final AccountService accountService;
    private final AccountNumberBusinessService accountNumberBusinessService;
    
    public AdminTransactionController(TransactionService transactionService, TransactionBusinessService transactionBusinessService, 
            TransactionOwnAccountsService transactionOwnAccountService, TransactionOwnAccountsBusinessService transactionOwnAccountsBusinessService,
            AccountService accountService, AccountNumberBusinessService accountNumberBusinessService){
            
        this.transactionService = transactionService;
        this.transactionBusinessService = transactionBusinessService;
        this.transactionOwnAccountService = transactionOwnAccountService;
        this.transactionOwnAccountsBusinessService = transactionOwnAccountsBusinessService;
        this.accountService = accountService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
      @GetMapping("/transaction-personal-admin")
        public String getAllPersonalAccountTransaction(Model model){
            List<TransactionModel> transaction = transactionService.getAllTransaction();
            model.addAttribute("transaction", transaction);
            return "transaction-personal-admin";
    }
        
      @GetMapping("/new-transaction-personal")
      public String getNewTransactionAdminPage(Model model){
          model.addAttribute("newTransactionAdmin",new TransactionModel());
          return "new-transaction-personal";
      }
      
      @GetMapping("/transaction-own-personal-admin")
      public String getAllOwnPersonalAccountTrasaction(Model model){
            List<TransactionOwnAccountsModel> transaction = transactionOwnAccountService.getAllTransaction();
            model.addAttribute("transaction", transaction);
            return "transaction-own-personal-admin";
      }
      
      @GetMapping("/new-own-transaction-personal")
      public String getNewOwnTransactionAdminPage(Model model){
        model.addAttribute("newOwnTransactionAdmin",new TransactionOwnAccountsModel());
          return "new-own-transaction-personal";
      }
      
      
      @PostMapping("/new-own-transaction-personal")
      public String transferMoney(@ModelAttribute TransactionOwnAccountsModel transactionOwnAccountsModel, RedirectAttributes redirectAttributes){
          
            if (transactionOwnAccountsModel.getFromAccountNumber().equals(transactionOwnAccountsModel.getToAccountNumber())) {
        redirectAttributes.addFlashAttribute("sameAccountError", "Sender and beneficiary accounts cannot be the same");
        return "redirect:/new-own-transaction-personal";
    }  
            
    try {
        // Step 1: Get account numbers
        String senderAccount = transactionOwnAccountsModel.getFromAccountNumber();
        String beneficiaryAccount = transactionOwnAccountsModel.getToAccountNumber();

        // Step 2: Fetch customer numbers
        String senderCustomerNumber = accountService.getCustomerNumberByAccountNumber(senderAccount);
        String beneficiaryCustomerNumber = accountService.getCustomerNumberByAccountNumber(beneficiaryAccount);

        // Step 3: Check if both accounts belong to the same customer
        if (!senderCustomerNumber.equals(beneficiaryCustomerNumber)) {
            redirectAttributes.addFlashAttribute("differentCustomerError", "Transfers are only allowed between own accounts.");
            return "redirect:/new-own-transaction-personal";
        }

        // Step 4: Set customer number and proceed
        transactionOwnAccountsModel.setCustomerNumber(senderCustomerNumber);
        transactionOwnAccountService.transferMoney(transactionOwnAccountsModel);

        return "redirect:/transaction-own-personal-admin";
        
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else if (e.getMessage().equals("Sender account not found")){
            redirectAttributes.addFlashAttribute("senderError", e.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/new-own-transaction-personal";
    }
   }
      
      @PostMapping("/new-transaction-personal")
public String transferMoney(@ModelAttribute TransactionModel transactionModel, RedirectAttributes redirectAttributes) {
    
            // Check if sender account number and beneficiary account number are the same
    if (transactionModel.getAccountNumber().equals(transactionModel.getAccountNumberBeneficiary())) {
        redirectAttributes.addFlashAttribute("sameAccountError", "Sender and beneficiary accounts cannot be the same");
        return "redirect:/new-transaction-personal";
    }    
    
    try {
        // Step 1: Get account numbers
        String senderAccount = transactionModel.getAccountNumber();
        String beneficiaryAccount = transactionModel.getAccountNumberBeneficiary();

        // Step 2: Get customer numbers from account numbers
        String senderCustomerNumber = accountService.getCustomerNumberByAccountNumber(senderAccount);
        String beneficiaryCustomerNumber = accountService.getCustomerNumberByAccountNumber(beneficiaryAccount);

        // Step 3: Check if both customer numbers are the same
        if (senderCustomerNumber.equals(beneficiaryCustomerNumber)) {
            redirectAttributes.addFlashAttribute("sameCustomerError", "Transfers between accounts of the same customer are not allowed.");
            return "redirect:/new-transaction-personal";
        }

        // Step 4: Save customer number to transaction and proceed
        transactionModel.setCustomerNumber(senderCustomerNumber);
        transactionService.transferMoney(transactionModel);

        return "redirect:/transaction-personal-admin";
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else if (e.getMessage().equals("Sender account not found")){
            redirectAttributes.addFlashAttribute("senderError", e.getMessage());
        }else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/new-transaction-personal";
    }
    
}
         
       
        @GetMapping("/transaction-business-admin")
        public String getAllBusinessAccountTransaction(Model model){
            List<TransactionBusinessModel> transactionBusiness = transactionBusinessService.getAllTransactionBusiness();
            model.addAttribute("transactionBusiness",transactionBusiness);
            return "transaction-business-admin";
        }
        
        @GetMapping("/new-transaction-business")
        public String getNewTransasctionAdminPage(Model model){
            model.addAttribute("newTransactionAdmin",new TransactionModel());
            return "new-transaction-business";
        }
        
        @GetMapping("/transaction-own-business-admin")
      public String getAllOwnBusinessAccountTransaction(Model model){
        List<TransactionOwnAccountsBusinessModel> transaction = transactionOwnAccountsBusinessService.getAllTransaction();
         model.addAttribute("transaction", transaction);
         return "transaction-own-business-admin";
      }
      
      @GetMapping("/new-own-transaction-business")
      public String getNewOwnBusinessTransactionPage(Model model){
        model.addAttribute("newOwnTransactionAdmin",new TransactionOwnAccountsBusinessModel());
          return "new-own-transaction-business";
      }
      
      @PostMapping("/new-own-transaction-business")
      public String transferMoney(@ModelAttribute TransactionOwnAccountsBusinessModel transactionOwnAccountsBusinessModel, RedirectAttributes redirectAttributes){
          
              if (transactionOwnAccountsBusinessModel.getFromAccountNumber().equals(transactionOwnAccountsBusinessModel.getToAccountNumber())) {
        redirectAttributes.addFlashAttribute("sameAccountError", "Sender and beneficiary accounts cannot be the same");
        return "redirect:/new-own-transaction-business";
    }  
    try {
        // Step 1: Get account numbers
        String senderAccount = transactionOwnAccountsBusinessModel.getFromAccountNumber();
        String beneficiaryAccount = transactionOwnAccountsBusinessModel.getToAccountNumber();

        // Step 2: Fetch customer numbers
        String senderCustomerNumber = accountNumberBusinessService.getCustomerNumberByAccountNumber(senderAccount);
        String beneficiaryCustomerNumber = accountNumberBusinessService.getCustomerNumberByAccountNumber(beneficiaryAccount);

        // Step 3: Check if both accounts belong to the same customer
        if (!senderCustomerNumber.equals(beneficiaryCustomerNumber)) {
            redirectAttributes.addFlashAttribute("differentCustomerError", "Transfers are only allowed between own accounts.");
            return "redirect:/new-own-transaction-business";
        }

        // Step 4: Set customer number and proceed
        transactionOwnAccountsBusinessModel.setCustomerNumber(senderCustomerNumber);
        
        transactionOwnAccountsBusinessService.transferMoney(transactionOwnAccountsBusinessModel);
        return "redirect:/transaction-own-business-admin";
        
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else if (e.getMessage().equals("Sendere account not found")){
           redirectAttributes.addFlashAttribute("senderError", e.getMessage());
        }else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
            return "redirect:/new-own-transaction-business";
    }
}
        
        @PostMapping("/new-transaction-business")
        public String transferMoney(@ModelAttribute TransactionBusinessModel transactionBusinessModel, RedirectAttributes redirectAttributes) {
            // Check if sender account number and beneficiary account number are the same
    if (transactionBusinessModel.getAccountNumber().equals(transactionBusinessModel.getAccountNumberBeneficiary())) {
        redirectAttributes.addFlashAttribute("sameAccountError", "Sender and beneficiary accounts cannot be the same");
        return "redirect:/new-transaction-business";
    }    
    
    try {
        // Step 1: Get account numbers
        String senderAccount = transactionBusinessModel.getAccountNumber();
        String beneficiaryAccount = transactionBusinessModel.getAccountNumberBeneficiary();

        // Step 2: Get customer numbers from account numbers
        String senderCustomerNumber = accountNumberBusinessService.getCustomerNumberByAccountNumber(senderAccount);
        String beneficiaryCustomerNumber = accountNumberBusinessService.getCustomerNumberByAccountNumber(beneficiaryAccount);

        // Step 3: Check if both customer numbers are the same
        if (senderCustomerNumber.equals(beneficiaryCustomerNumber)) {
            redirectAttributes.addFlashAttribute("sameCustomerError", "Transfers between accounts of the same customer are not allowed.");
            return "redirect:/new-transaction-business";
        }

        // Step 4: Save customer number to transaction and proceed
        transactionBusinessModel.setCustomerNumber(senderCustomerNumber);
        
        transactionBusinessService.transferMoney(transactionBusinessModel);
        return "redirect:/transaction-business-admin";
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else if (e.getMessage().equals("Sender account not found")){
            redirectAttributes.addFlashAttribute("senderError", e.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/new-transaction-business";
    }
}
}
