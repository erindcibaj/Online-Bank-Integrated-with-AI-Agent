package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.AccountModel;
import com.example.bank.model.CardModel;
import com.example.bank.model.DepositModel;
import com.example.bank.model.LoanModel;
import com.example.bank.model.TransactionModel;
import com.example.bank.service.AccountService;
import com.example.bank.service.CardService;
import com.example.bank.service.DepositService;
import com.example.bank.service.LoanService;
import com.example.bank.service.TransactionService;
import com.example.bank.service.UsersService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    private final AccountService accountService;
    private final CardService cardService;
    private final DepositService depositService;
    private final LoanService loanService;
    private final UsersService usersService;
    private final TransactionService transactionService;
    
    public DashboardController(AccountService accountService, CardService cardService, DepositService depositService, 
            LoanService loanService, UsersService usersService, TransactionService transactionService){
        this.accountService = accountService;
        this.cardService = cardService;
        this.depositService = depositService;
        this.loanService = loanService;
        this.usersService = usersService;
        this.transactionService = transactionService;
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        String customerNumber = (String) session.getAttribute("customerNumber");

        List<AccountModel> accounts = accountService.getAccount(customerNumber);
        model.addAttribute("accounts", accounts);
        
        List<CardModel> cards = cardService.getCardsByCustomerNumber(customerNumber);
        
        // Decrypt card numbers before displaying
        for (CardModel card : cards) {
            try {
                String decryptedCardNumber = EncryptionUtil.decrypt(card.getCardNumber());
                card.setCardNumber(decryptedCardNumber);
            } catch (Exception e) {
                card.setCardNumber("Error");
            }
        }
        
        model.addAttribute("cards", cards);
        
        // Fetch only the deposits associated with this customer
        List<DepositModel> deposits = depositService.getDepositsByCustomerNumber(customerNumber);
        model.addAttribute("deposit", deposits);
        
        List<LoanModel> loans = loanService.getLoansByCustomerNumber(customerNumber);
        model.addAttribute("loans", loans);
        
        String fullName = usersService.getUserFullName(customerNumber);
        model.addAttribute("fullName", fullName);
        
        List<TransactionModel> transactions = transactionService.get5TransactionsByCustomerNumber(customerNumber);
        model.addAttribute("transaction", transactions);
        
        return "dashboard";
    }
}
