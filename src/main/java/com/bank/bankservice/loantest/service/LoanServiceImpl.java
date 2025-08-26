package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.dao.LoanRepository;
import com.bank.bankservice.loantest.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{

    @Autowired
    LoanRepository loanRepository;

    @Override
    public int getLoanCount() {
        return loanRepository.getLoanCount();
    }

    @Override
    public int getLoanCount(int userId) {
        return loanRepository.getLoanCount(userId);
    }

    @Override
    public List<Loan> getLoanList() {
        List<Loan> loanList = loanRepository.getLoanList();
        return loanList;
    }

    @Override
    public Loan getLoanInfo(int loanId, int userId) {
        Loan loan =  loanRepository.getLoanInfo(loanId, userId);
        return loan;
    }

    @Override
    public void insertLoan(Loan board) {
        loanRepository.insertLoan(board);
    }

    @Override
    public void updateLoan(Loan board) {
        loanRepository.updateLoan(board);
    }

    @Override
    public int deleteLoan(int loanId, int userId) {
        return loanRepository.deleteLoan(loanId, userId);
    }
}
