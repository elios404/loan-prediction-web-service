package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.dao.LoanRepository;
import com.bank.bankservice.loantest.dao.LoanTestRepository;
import com.bank.bankservice.loantest.dao.UserRepository;
import com.bank.bankservice.loantest.dto.LoanTest;
import com.bank.bankservice.loantest.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanTestServiceImpl implements LoanTestService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    LoanTestRepository loanTestRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public int getTestCount() {
        return loanTestRepository.getTestCount();
    }

    @Override
    public int getTestCount(int userId, int loanId) {
        return loanTestRepository.getTestCount(userId, loanId);
    }

    @Override
    public List<LoanTest> getTestList() {
        return loanTestRepository.getTestList();
    }

    @Override
    public LoanTest getTestInfo(int userId, int loanId) {
        return loanTestRepository.getTestInfo(userId, loanId);
    }

    @Override
    public void insertTest(Loan loan) {
        loanRepository.insertLoan(loan);
    }

    @Override
    public void updateTest(Loan loan) {
        loanRepository.updateLoan(loan);

    }

    @Override
    public int deleteTest(int userId, int loanId) {
        return loanRepository.deleteLoan(userId, loanId);
    }
}
