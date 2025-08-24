
package com.example.bank.controller;

import com.example.bank.model.TransactionOwnAccountsModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionOwnAccountsService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionOwnAccountController {
    
     private final TransactionOwnAccountsService transactionOwnAccountsService;
      private final AccountService accountService;
        
    public TransactionOwnAccountController(TransactionOwnAccountsService transactionOwnAccountsService, AccountService accountService) {
        this.transactionOwnAccountsService = transactionOwnAccountsService;
        this.accountService = accountService;
    }
    
         @GetMapping("/transaction-own-accounts-list")
    public String getTransactionOwnAccountListPage(Model model, HttpSession session) {
        // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/login";
        }

        // Fetch only the transactions related to this user
        List<TransactionOwnAccountsModel> transaction = transactionOwnAccountsService.getTransactionsByCustomerNumber(customerNumber);
         model.addAttribute("transaction", transaction);
         return "transaction-own-accounts-list";
    }
    
    @GetMapping("/transaction-own-accounts-personal")
    public String getTransactionOwnAccountsPersonalPage(Model model, HttpSession session){
                   // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            // Redirect to login if session is missing
            return "redirect:/login";
        }

        // Fetch only the account numbers associated with this customer
        List<String> accountNumbers = accountService.getAccountsByCustomerNumber(customerNumber);
        model.addAttribute("accounts",accountNumbers);
        model.addAttribute("newTransaction",new TransactionOwnAccountsModel());
        return "transaction-own-accounts-personal";
    }
    
    @PostMapping("/transaction-own-accounts-personal")
public String transferMoney(@ModelAttribute TransactionOwnAccountsModel transactionOwnAccountsModel, RedirectAttributes redirectAttributes, HttpSession session) {
    
            // Retrieve the logged-in customer's number from the session
        String customerNumber = (String) session.getAttribute("customerNumber");

        if (customerNumber == null) {
            return "redirect:/login";
        }

        // Assign the logged-in customer's number to the transaction
        transactionOwnAccountsModel.setCustomerNumber(customerNumber);
    
    try {       
        //Get sender's full name using sender's account number
        String senderAccountNumber = transactionOwnAccountsModel.getFromAccountNumber(); 
        String fullName = accountService.getFullNameByAccountNumber(senderAccountNumber); 

        //Set full name in the transaction
        transactionOwnAccountsModel.setName(fullName);
        
        transactionOwnAccountsService.transferMoney(transactionOwnAccountsModel);
        return "redirect:/transaction-own-accounts-list";
    } catch (RuntimeException e) {
        if (e.getMessage().equals("Beneficiary account not found")) {
            redirectAttributes.addFlashAttribute("beneficiaryError", e.getMessage());
        } else if (e.getMessage().equals("Insufficient funds")) {
            redirectAttributes.addFlashAttribute("fundsError", e.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction-own-accounts-personal";
    }
}
}
