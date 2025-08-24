
package com.example.bank.controller;

import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.AccountService;
import com.example.bank.service.CardBusinessService;
import com.example.bank.service.CardService;
import com.example.bank.service.LoanBusinessService;
import com.example.bank.service.LoanService;
import com.example.bank.service.TransactionBusinessService;
import com.example.bank.service.TransactionService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {
    
    private final AccountService accountService;
    private final AccountNumberBusinessService accountNumberBusinessService;
    private final LoanService loanService;
    private final LoanBusinessService loanBusinessService;
    private final CardService cardService;
    private final CardBusinessService cardBusinessService;
    private final TransactionService transactionService;
    private final TransactionBusinessService transactionBusinessService;
    
    public AdminDashboardController(AccountService accountService, AccountNumberBusinessService accountNumberBusinessService, 
            LoanService loanService, LoanBusinessService loanBusinessService, CardService cardService, CardBusinessService cardBusinessService,
            TransactionService transactionService, TransactionBusinessService transactionBusinessService){
        
        this.accountService = accountService;
        this.accountNumberBusinessService = accountNumberBusinessService;
        this.loanService = loanService;
        this.loanBusinessService = loanBusinessService;
        this.cardService = cardService;
        this.cardBusinessService = cardBusinessService;
        this.transactionService = transactionService;
        this.transactionBusinessService = transactionBusinessService;
    }
    
    @GetMapping("/admin-dashboard")
    public String getAdminDashboardPage(Model model) {
        Map<String, Integer> personalAccounts = accountService.getAccountStats();
        model.addAttribute("personalAccounts", personalAccounts);
        Map<String, Integer> businessAccounts = accountNumberBusinessService.getAccountStats();
        model.addAttribute("businessAccounts", businessAccounts);
        
        Map<String, Integer> transactionpersonalAccounts = transactionService.getSumTransactionPersonal();
        model.addAttribute("transactionpersonalAccounts", transactionpersonalAccounts);
        Map<String, Integer> transactionbusinessAccounts = transactionBusinessService.getSumTransactionBusiness();
        model.addAttribute("transactionbusinessAccounts", transactionbusinessAccounts);
        
        model.addAttribute("loanTypesPersonal", loanService.countAccountsByLoanType());
        model.addAttribute("loanTypesBusiness", loanBusinessService.countAccountsByLoanType());
        
       model.addAttribute("cardTypesPersonal", cardService.countAccountsByCardType());
       model.addAttribute("cardTypesBusiness", cardBusinessService.countAccountsByCardType());
        return "admin-dashboard";
    }
}
