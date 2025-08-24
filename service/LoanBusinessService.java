
package com.example.bank.service;

import com.example.bank.model.LoanBusinessModel;
import com.example.bank.repository.LoanBusinessRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LoanBusinessService {
    
    
private final LoanBusinessRepository loanBusinessRepository;
    
    public LoanBusinessService(LoanBusinessRepository loanBusinessRepository) {
        this.loanBusinessRepository = loanBusinessRepository;
    }
    

    public LoanBusinessModel saveLoan(LoanBusinessModel loanBusinessModel) {
        return loanBusinessRepository.save(loanBusinessModel);
    }
    
           public List<LoanBusinessModel> getAllBusinessLoans() {
        return loanBusinessRepository.findAll(); 
    }
           
    @Transactional
    public void updateLoanStatusBusiness(Integer loanId, String status) {
        LoanBusinessModel loan = loanBusinessRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(status);
        loanBusinessRepository.save(loan);
    }
    
        public Map<String, Long> countAccountsByLoanType() {
        List<Object[]> results = loanBusinessRepository.countAccountsByLoanType();
        Map<String, Long> loanCounts = new HashMap<>();
        for (Object[] result : results) {
            loanCounts.put((String) result[0], (Long) result[1]);
        }
        return loanCounts;
    }
        
        public List<LoanBusinessModel> getLoansByCustomerNumber(String customerNumber) {
    return loanBusinessRepository.findByCustomerNumber(customerNumber);
}

}
