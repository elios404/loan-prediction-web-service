package com.bank.bankservice.loantest.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    // 대출 평가에 필요한 개인의 금융 정보를 담는 객체
    // 개인은 여러 개의 금융 정보 객체와 DB에서 연결될 수 있음

    private int loanId;
    private int userId;
    private double income;
    private int empExp;
    private float amount;
    private String intent;
    private double intRate;
    private double loanPercentIncome;
    private double credHistLength;
    private int creditScore;
    private int previousLoanDefaults;
    private int loanStatus;
    private List<Double> loanExplain;
}
