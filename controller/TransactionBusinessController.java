
package com.example.bank.controller;

import com.example.bank.model.TransactionBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.TransactionBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionBusinessController {
    
      private final TransactionBusinessService transactionBusinessService;
      private final AccountNumberBusinessService accountNumberBusinessService;
        
    public TransactionBusinessController(TransactionBusinessService transactionBusinessService, AccountNumberBusinessService accountNumberBusinessService) {
        this.transactionBusinessService = transactionBusinessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
     @GetMapping("/transaction-list-business")
    public String getTransactionListBusinessPage(Model model, HttpSession session) {
        // Retrieve the logged-in business customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/loginBusiness";
        }

        // Fetch only the transactions associated with this business customer
        List<TransactionBusinessModel> transaction = transactionBusinessService.getTransactionsByCustomerNumber(customerNumber);
         model.addAttribute("transaction", transaction);
         return "transaction-list-business";
    }
    
    
    @GetMapping("/transaction-business")
    public String getTransactionBusinessPage(Model model, HttpSession session){
                           // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/loginBusiness";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newTransactionBusiness",new TransactionBusinessModel());
        return "transaction-business";
    }
    
    @PostMapping("/transaction-business")
public String transferMoney(@ModelAttribute TransactionBusinessModel transactionBusinessModel, RedirectAttributes redirectAttributes, HttpSession session) {
    
            // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Assign the logged-in customer's number to the transaction
        transactionBusinessModel.setCustomerNumber(customerNumber);
        
            if (transactionBusinessModel.getAccountNumber().equals(transactionBusinessModel.getAccountNumberBeneficiary())) {
        redirectAttributes.addFlashAttribute("sameAccountError", "Sender and beneficiary accounts cannot be the same");
        return "redirect:/transaction-personal";
    }
            
                    // Check if beneficiary account also belongs to the same customer (own account transfer)
    List<String> userAccounts = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);
    if (userAccounts.contains(transactionBusinessModel.getAccountNumberBeneficiary())) {
        redirectAttributes.addFlashAttribute("ownAccountError", "Transfers to your own accounts are not allowed");
        return "redirect:/transaction-personal";
    }
    
    try {
        transactionBusinessService.transferMoney(transactionBusinessModel);
        return "redirect:/transaction-list-business";
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction-business";
    }
}

@GetMapping("/received-transactions-business")
public String getReceivedTransactions(Model model, HttpSession session) {
    // Retrieve the logged-in customer's number from the session
    String customerNumber = (String) session.getAttribute("customerNumber");

    if (customerNumber == null) {
        // Redirect to login if session is missing
        return "redirect:/loginBusiness";
    }

    List<String> userAccounts = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);

    List<TransactionBusinessModel> receivedTransactions = transactionBusinessService.findByBeneficiaryAccounts(userAccounts);

    // Replace the senderName with real name based on account number
    for (TransactionBusinessModel transaction : receivedTransactions) {
        String senderAccountNumber = transaction.getAccountNumber();
        String actualSenderName = accountNumberBusinessService.getFullNameByAccountNumber(senderAccountNumber);
        transaction.setName(actualSenderName); 
    }

    model.addAttribute("receivedTransactions", receivedTransactions);
    return "received-transactions-business";
}

}
