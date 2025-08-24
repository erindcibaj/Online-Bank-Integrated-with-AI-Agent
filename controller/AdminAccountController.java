package com.example.bank.controller;

import com.example.bank.model.AccountModel;
import com.example.bank.model.AccountNumberBusinessModel;
import com.example.bank.model.BusinessAccount;
import com.example.bank.model.UsersModel;
import com.example.bank.service.AccountNumberBusinessService;
import com.example.bank.service.AccountService;
import com.example.bank.service.BusinessService;
import com.example.bank.service.UsersService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminAccountController {
    
    private final AccountService accountService; 
    private final AccountNumberBusinessService accountNumberBusinessService;
    private final UsersService usersService;
    private final BusinessService businessService;
    
    public AdminAccountController(AccountService accountService, AccountNumberBusinessService accountNumberBusinessService, UsersService usersService, BusinessService businessService){
        this.accountService = accountService;
        this.accountNumberBusinessService = accountNumberBusinessService;
        this.usersService = usersService;
        this.businessService = businessService;
    }
      
    @GetMapping("new-account")
    public String getNewAccountPage(Model model){
        AccountModel account = new AccountModel();
        account.setAccountNumber(accountService.generateAccountNumber());
        model.addAttribute("account", account);
        return "new-account";
    }
    
    @PostMapping("/new-account")
    public String submitAccount(@ModelAttribute AccountModel accountModel, BindingResult result){
        
    accountService.saveAccount(accountModel);
    return "redirect:/personal";
        }
    
    
    @GetMapping("/personal")
    public String getPersonalAccountPage(Model model){
        List<AccountModel> account = accountService.getAllAccounts(); 
        model.addAttribute("list", account);
        return "personal";
        }
    
    @GetMapping("/business")
    public String getBusinessAccountPage(Model model){
         List<AccountNumberBusinessModel> accountBusiness = accountNumberBusinessService.getAllAccounts(); 
         model.addAttribute("businesslist", accountBusiness);
        return "business";
        }
    
    @GetMapping("/new-account-business")
    public String getNewAccountBusinessPage(Model model){
        AccountNumberBusinessModel accountBusiness = new AccountNumberBusinessModel();
        accountBusiness.setAccountNumber(accountService.generateAccountNumber());
        model.addAttribute("accountBusiness",accountBusiness);
        return "new-account-business";
        }
    
    @PostMapping("/new-account-business")
    public String submitAccountBusiness(@ModelAttribute AccountNumberBusinessModel accountNumberBusinessModel, BindingResult result){
    
    accountNumberBusinessService.saveAccount(accountNumberBusinessModel);
    return "redirect:/business";
        }
    
        @GetMapping("/edit-account/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        AccountModel accountModel = accountService.getAccountById(id);
        model.addAttribute("account", accountModel);
        return "edit-account";  
    }

    @PostMapping("/update-account")
    public String updateAccount(@ModelAttribute AccountModel accountModel) {
        accountService.updateAccount(accountModel);
        return "redirect:/personal"; 
    }
    
            @GetMapping("/edit-account-business/{id}")
    public String showEditBusinessForm(@PathVariable Integer id, Model model) {
        AccountNumberBusinessModel accountNumberBusinessModel = accountNumberBusinessService.getAccountById(id);
        model.addAttribute("account", accountNumberBusinessModel);
        return "edit-account-business";  
    }

    @PostMapping("/update-account-business")
    public String updateBusinessAccount(@ModelAttribute AccountNumberBusinessModel accountNumberBusinessModel) {
        accountNumberBusinessService.updateAccount(accountNumberBusinessModel);
        return "redirect:/business"; 
    }
    
    @GetMapping("/customer-list")
    public String customerList(Model model){
        List<UsersModel> customerList = usersService.getAllCustomers()
        .stream()
        .filter(customer -> customer.getAdmin() != 1)  // Exclude admin users
        .collect(Collectors.toList());

         model.addAttribute("customer", customerList);
        return "customer-list";
    }
    
    @GetMapping("/edit-customer/{id}")
    public String showEditCustomerForm(@PathVariable Integer id, Model model){
        UsersModel usersModel = usersService.getCustomerById(id);
        model.addAttribute("customer", usersModel);
        return "edit-customer";
    }
    
    @PostMapping("/update-customer")
    public String updateCustomerList(@ModelAttribute UsersModel usersModel){
        usersService.updateCustomer(usersModel);
        return "redirect:/customer-list"; 
    }
    
    
        @GetMapping("/customer-business-list")
    public String customerBusinessList(Model model){
        List<BusinessAccount> customerList = businessService.getAllCustomers(); 
         model.addAttribute("customer", customerList);
        return "customer-business-list";
    }
    
        @GetMapping("/edit-customer-business/{id}")
    public String showEditCustomerBusinessForm(@PathVariable Integer id, Model model){
        BusinessAccount businessAccount = businessService.getCustomerById(id);
        model.addAttribute("customer", businessAccount);
        return "edit-customer-business";
    }
    
        @PostMapping("/update-customer-business")
    public String updateCustomerBusinessList(@ModelAttribute BusinessAccount businessAccount){
        businessService.updateCustomer(businessAccount);
        return "redirect:/customer-business-list"; 
    }
    
}
