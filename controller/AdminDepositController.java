
package com.example.bank.controller;

import com.example.bank.model.DepositBusinessModel;
import com.example.bank.model.DepositModel;
import com.example.bank.service.DepositBusinessService;
import com.example.bank.service.DepositService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDepositController {
    
    private final DepositService depositService;
    private final DepositBusinessService depositBusinessService;
    
    public AdminDepositController(DepositService depositService, DepositBusinessService depositBusinessService){
        this.depositService = depositService;
        this.depositBusinessService = depositBusinessService;
    }
    
          @GetMapping("/deposit-personal-admin")
        public String getAllPersonalAccountCard(Model model){
            List<DepositModel> deposit = depositService.getAllDeposit();
            model.addAttribute("deposit", deposit);
            return "deposit-personal-admin";
    }
        
        @GetMapping("/deposit-business-admin")
        public String getAllBusinessAccountCard(Model model){
            List<DepositBusinessModel> depositBusiness = depositBusinessService.getAllDepositBusiness();
            model.addAttribute("depositBusiness",depositBusiness);
            return "deposit-business-admin";
        }
}
