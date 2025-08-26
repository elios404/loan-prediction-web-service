package com.bank.bankservice.loantest.dao;

import com.bank.bankservice.loantest.dto.LoanTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 조회를 전용으로 하는 레포 (join을 통해서)
@Mapper
public interface LoanTestRepository {
    int getTestCount();
    int getTestCount(@Param("loanId") int loanId, @Param("userId") int userId);

    List<LoanTest> getTestList();
    LoanTest getTestInfo(int loanId, int userId);
}
