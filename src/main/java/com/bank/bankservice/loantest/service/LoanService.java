package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.model.Loan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanService {
    int getLoanCount();
    int getLoanCount(@Param("loanId") int loanId);

    List<Loan> getLoanList();
    Loan getLoanInfo(int loanId);

    void insertLoan(Loan loan);
    void updateLoan(Loan loan);
    int deleteLoan(int userId, int loanId);
}
