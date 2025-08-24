
package com.example.bank.controller;

import com.example.bank.model.TransactionOwnAccountsBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.TransactionOwnAccountsBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionOwnAccountsBusinessController {
    
    private final TransactionOwnAccountsBusinessService transactionOwnAccountsBusinessService;
      private final AccountNumberBusinessService accountNumberBusinessService;
        
    public TransactionOwnAccountsBusinessController(TransactionOwnAccountsBusinessService transactionOwnAccountsBusinessService, AccountNumberBusinessService accountNumberBusinessService) {
        this.transactionOwnAccountsBusinessService = transactionOwnAccountsBusinessService;
        this.accountNumberBusinessService = accountNumberBusinessService;
    }
    
         @GetMapping("/transaction-own-accounts-list-business")
    public String getTransactionOwnAccountListBusinessPage(Model model, HttpSession session) {
        // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Fetch only the transactions related to this user
        List<TransactionOwnAccountsBusinessModel> transaction = transactionOwnAccountsBusinessService.getTransactionsByCustomerNumber(customerNumber);
         model.addAttribute("transaction", transaction);
         return "transaction-own-accounts-list-business";
    }
    
    @GetMapping("/transaction-own-accounts-business")
    public String getTransactionOwnAccountsPersonalPage(Model model, HttpSession session){
                   // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/loginBusiness";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountNumberBusinessService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newTransaction",new TransactionOwnAccountsBusinessModel());
        return "transaction-own-accounts-business";
    }
    
    @PostMapping("/transaction-own-accounts-business")
public String transferMoney(@ModelAttribute TransactionOwnAccountsBusinessModel transactionOwnAccountsBusinessModel, RedirectAttributes redirectAttributes, HttpSession session) {
    
            // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/loginBusiness";
        }

        // Assign the logged-in customer's number to the transaction
        transactionOwnAccountsBusinessModel.setCustomerNumber(customerNumber);
    
    try {
        
        //Get sender's full name using sender's account number
        String senderAccountNumber = transactionOwnAccountsBusinessModel.getFromAccountNumber(); 
        String fullName = accountNumberBusinessService.getFullNameByAccountNumber(senderAccountNumber); 

        //Set full name in the transaction
        transactionOwnAccountsBusinessModel.setName(fullName);
        
        transactionOwnAccountsBusinessService.transferMoney(transactionOwnAccountsBusinessModel);
        return "redirect:/transaction-own-accounts-list-business";
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction-own-accounts-business";
    }
}
}
