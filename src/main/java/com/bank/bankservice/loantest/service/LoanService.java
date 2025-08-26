package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.model.Loan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanService {
    int getLoanCount();
    int getLoanCount(@Param("userId") int userId);

    List<Loan> getLoanList();
    Loan getLoanInfo(int loanId, int userId);

    void insertLoan(Loan loan);
    void updateLoan(Loan loan);
    int deleteLoan(int loanId, int userId);
}
