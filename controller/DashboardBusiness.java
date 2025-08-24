package com.example.bank.controller;

import com.example.bank.EncryptionUtil;
import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.CardBusinessModel;
import com.example.bank.model.DepositBusinessModel;
import com.example.bank.model.LoanBusinessModel;
import com.example.bank.model.TransactionBusinessModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.CardBusinessService;
import com.example.bank.service.DepositBusinessService;
import com.example.bank.service.LoanBusinessService;
import com.example.bank.service.TransactionBusinessService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardBusiness {
    
    private final AccountNumberBusinessService accountNumberBusinessService;
    private final CardBusinessService cardBusinessService;
    private final DepositBusinessService depositBusinessService;
    private final LoanBusinessService loanBusinessService;
    private final TransactionBusinessService transactionBusinessService;
    
    public DashboardBusiness(AccountNumberBusinessService accountNumberBusinessService, CardBusinessService cardBusinessService,
            DepositBusinessService depositBusinessService, LoanBusinessService loanBusinessService, TransactionBusinessService transactionBusinessService){
        this.accountNumberBusinessService = accountNumberBusinessService;
        this.cardBusinessService = cardBusinessService;
        this.depositBusinessService = depositBusinessService;
        this.loanBusinessService = loanBusinessService;
        this.transactionBusinessService = transactionBusinessService;
    }
    
    @GetMapping("dashboard-business")
    public String getBusinessDashboardPage(Model model, HttpSession session){
        
        String customerNumber = (String) session.getAttribute("customerNumber");
        if (customerNumber == null) {
            return "redirect:/loginBusiness"; // Redirect to login if no customer number is found
        }

        List<AccountNumberBusinessModel> accounts = accountNumberBusinessService.getAccount(customerNumber);
        model.addAttribute("accounts", accounts);
        
        // Fetch cards associated with the business customer
        List<CardBusinessModel> cards = cardBusinessService.getCardsByCustomerNumber(customerNumber);
        
        // Decrypt card numbers before displaying
        for (CardBusinessModel card : cards) {
            try {
                String decryptedCardNumber = EncryptionUtil.decrypt(card.getCardNumber());
                card.setCardNumber(decryptedCardNumber);
            } catch (Exception e) {
                card.setCardNumber("Error");
            }
        }
        
        model.addAttribute("cards", cards);
        
        // Fetch only the deposits associated with this customer
        List<DepositBusinessModel> deposits = depositBusinessService.getDepositsByCustomerNumber(customerNumber);
        model.addAttribute("deposit", deposits);
        
        List<LoanBusinessModel> loans = loanBusinessService.getLoansByCustomerNumber(customerNumber);
        model.addAttribute("loans", loans);
        
        List<TransactionBusinessModel> transactions = transactionBusinessService.get5TransactionsByCustomerNumber(customerNumber);
        model.addAttribute("transaction", transactions);
        
        return "dashboard-business";
    }
}
