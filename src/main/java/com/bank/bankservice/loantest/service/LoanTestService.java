package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.dto.LoanTest;
import com.bank.bankservice.loantest.model.Loan;
import com.bank.bankservice.loantest.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanTestService {
    int getTestCount();
    int getTestCount(@Param("userId") int userId, @Param("loanId") int loanId);

    List<LoanTest> getTestList();
    LoanTest getTestInfo(int userId, int loanId);

    void insertTest(Loan loan);
    void updateTest(Loan loan);
    int deleteTest(int userId, int loanId);

}
