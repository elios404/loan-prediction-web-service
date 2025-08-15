package com.bank.bankservice.loantest.dao;

import com.bank.bankservice.loantest.dto.LoanTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 조회를 전용으로 하는 레포 (join을 통해서)
@Mapper
public interface LoanTestRepository {
    int getTestCount();
    int getTestCount(@Param("userId") int userId, @Param("loanId") int loanId);

    List<LoanTest> getTestList();
    LoanTest getTestInfo(int userId, int loanId);
}
