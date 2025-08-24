package com.bank.bankservice.loantest.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTest {
    
    // 대출 평가와 관련하여, 유저정보와 유저의 금융 정보를 함께 사용하기 위한 객체

    // 유저 정보
    private int userId;
    private String name;
    private double age;
    private int gender;
    private String education;
    private String homeOwnership;

    // 금융 정보
    private int loanId;
//    private int userId; //중복되는 속성, DB에서 user, loan 테이블의 join을 위해서
    private double income;
    private int empExp;
    private float amount;
    private String intent;
    private double intRate;
    private double loanPercentIncome;
    private double credHistLength;
    private int creditScore;
    private int previousLoanDefaults;
    private int loanStatus = -1;
    private List<Double> loanExplain;

}
