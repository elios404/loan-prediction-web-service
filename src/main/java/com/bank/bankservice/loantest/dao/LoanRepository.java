package com.bank.bankservice.loantest.dao;

import com.bank.bankservice.loantest.model.Loan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoanRepository {
    
    // DB에서 loan 테이블에 접근하기 위한 repository, LoanMapper.xml에 구현됨
    
    int getLoanCount();
    int getLoanCount(@Param("userId") int userId);

    List<Loan> getLoanList(); // 전체 금융 정보의 목록를 loan 객체 리스트로 반환
    Loan getLoanInfo(int loanId, int userId); // 특정 loanId 를 가진 금융 정보를 Loan 객체로 반환

    void insertLoan(Loan loan); // 새로운 금융 정보를 insert
    void updateLoan(Loan loan); // 기존의 금융 정보를 update
    int deleteLoan(int loanId, int userId); // 기존의 금융 정보를 delete
}
