
package com.example.bank.controller;

import com.example.bank.model.TransactionModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionController {
    
      private final TransactionService transactionService;
      private final AccountService accountService;
        
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }
    
@GetMapping("/transaction-list")
public String getTransactionListPage(Model model, HttpSession session) {
    // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        // Redirect to login if session is missing
        return "redirect:/login";
    }

    // Fetch only the transactions related to this customer
    List<TransactionModel> transactions = transactionService.getTransactionsByCustomerNumber(customerNumber);
    model.addAttribute("transaction", transactions);
    
    return "transaction-list";
}

    
    @GetMapping("/transaction-personal")
    public String getTransactionPersonalPage(Model model, HttpSession session){
                   // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/login";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newTransaction",new TransactionModel());
        return "transaction-personal";
    }
    
    @PostMapping("/transaction-personal")
public String transferMoney(@ModelAttribute TransactionModel transactionModel, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
    
            // Retrieve customer number from session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";
    }

    // Set the customer number in cardModel
    transactionModel.setCustomerNumber(customerNumber);
    
        // Check if sender account number and beneficiary account number are the same
    if (transactionModel.getAccountNumber().equals(transactionModel.getAccountNumberBeneficiary())) {
        redirectAttributes.addFlashAttribute("sameAccountError", "Sender and beneficiary accounts cannot be the same");
        return "redirect:/transaction-personal";
    }    
    
        // Check if beneficiary account also belongs to the same customer (own account transfer)
    List<String> userAccounts = accountService.getAccountsByCustomerNumber(customerNumber);
    if (userAccounts.contains(transactionModel.getAccountNumberBeneficiary())) {
        redirectAttributes.addFlashAttribute("ownAccountError", "Transfers to own accounts are not allowed");
        return "redirect:/transaction-personal";
    }
    
    
    try {
        transactionService.transferMoney(transactionModel);
        return "redirect:/transaction-list";
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction-personal";
    }
}

@GetMapping("/received-transactions")
public String getReceivedTransactions(Model model, HttpSession session) {
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        return "redirect:/login";
    }

    List<String> userAccounts = accountService.getAccountsByCustomerNumber(customerNumber);

    List<TransactionModel> receivedTransactions = transactionService.findByBeneficiaryAccounts(userAccounts);

    // Replace the senderName with real name based on account number
    for (TransactionModel transaction : receivedTransactions) {
        String senderAccountNumber = transaction.getAccountNumber();
        String actualSenderName = accountService.getFullNameByAccountNumber(senderAccountNumber);
        transaction.setName(actualSenderName); 
    }

    model.addAttribute("receivedTransactions", receivedTransactions);
    return "received-transactions";
}





}


