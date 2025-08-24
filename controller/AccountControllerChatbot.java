package com.example.bank.controller;

import com.example.bank.service.AccountService;
import com.example.bank.service.AccountNumberBusinessService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountControllerChatbot {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountNumberBusinessService accountNumberBusinessService;

    @GetMapping("/balance")
    public ResponseEntity<?> getAccountBalance(
            @RequestParam String accountNumber,
            HttpSession session) {

        String customerNumber = (String) session.getAttribute("customerNumber");

        try {
            // First, try to get balance from normal account
            Map<String, Object> balanceInfo = accountService.getBalanceForAccount(customerNumber, accountNumber);
            return ResponseEntity.ok(balanceInfo);

        } catch (Exception e1) {
            try {
                // If not found, try to get balance from business account
                Map<String, Object> balanceInfoBusiness = accountNumberBusinessService.getBalanceForAccount(customerNumber, accountNumber);
                return ResponseEntity.ok(balanceInfoBusiness);

            } catch (Exception e2) {
                // If both fail, return error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Account not found or not linked to your profile.");
            }
        }
    }
}
