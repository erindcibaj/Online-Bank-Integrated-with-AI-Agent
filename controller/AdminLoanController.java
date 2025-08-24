
package com.example.bank.controller;

import com.example.bank.model.LoanBusinessModel;
import com.example.bank.model.LoanModel;
import com.example.bank.service.LoanBusinessService;
import com.example.bank.service.LoanService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminLoanController {
    
    private final LoanService loanService;
    private final LoanBusinessService loanBusinessService;
    
    public AdminLoanController(LoanService loanService, LoanBusinessService loanBusinessService){
        this.loanService = loanService;
        this.loanBusinessService = loanBusinessService;
    }
    
      @GetMapping("/loan-personal-admin")
        public String getAllPersonalAccountLoan(Model model){
            List<LoanModel> loans = loanService.getAllLoans(); 
            model.addAttribute("loans", loans);
            return "loan-personal-admin";
    }
        
        @PostMapping("/updateStatus")
public String updateLoanStatus(@RequestParam Integer loanId, 
                               @RequestParam String status, 
                               RedirectAttributes redirectAttributes) {
    loanService.updateLoanStatus(loanId, status);
    redirectAttributes.addFlashAttribute("message", "Loan status updated successfully!");
    return "redirect:/loan-personal-admin";
}
        
        @GetMapping("/loan-business-admin")
        public String getAllBusinessAccountLoan(Model model){
            List<LoanBusinessModel> loansBusiness = loanBusinessService.getAllBusinessLoans();
            model.addAttribute("loansBusiness",loansBusiness);
            return "loan-business-admin";
        }
        
        @PostMapping("/updateStatusBusiness")
        public String updateLoanStatusBusiness(@RequestParam Integer loanId, 
                               @RequestParam String status, 
                               RedirectAttributes redirectAttributes) {
    loanBusinessService.updateLoanStatusBusiness(loanId, status);
    redirectAttributes.addFlashAttribute("message", "Loan status updated successfully!");
    return "redirect:/loan-business-admin";
    }
}           
