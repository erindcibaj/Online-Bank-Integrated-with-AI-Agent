
package com.example.bank.service;

import com.example.bank.model.LoanModel;
import com.example.bank.repository.LoanRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
   
    private final LoanRepository loanRepository;
    
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    public LoanModel saveLoan(LoanModel loanModel){
        
        return loanRepository.save(loanModel);
        
    }
    
       public List<LoanModel> getAllLoans() {
        return loanRepository.findAll(); 
    }
       
    @Transactional
    public void updateLoanStatus(Integer loanId, String status) {
        LoanModel loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(status);
        loanRepository.save(loan);
    }
    
        public Map<String, Long> countAccountsByLoanType() {
        List<Object[]> results = loanRepository.countAccountsByLoanType();
        Map<String, Long> loanCounts = new HashMap<>();
        for (Object[] result : results) {
            loanCounts.put((String) result[0], (Long) result[1]);
        }
        return loanCounts;
    }
        
        public List<LoanModel> getLoansByCustomerNumber(String customerNumber) {
    return loanRepository.findByCustomerNumber(customerNumber);
}

}
