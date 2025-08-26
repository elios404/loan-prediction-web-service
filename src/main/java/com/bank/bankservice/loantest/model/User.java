package com.bank.bankservice.loantest.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    // 각 유저의 개인 정보를 담는 객체
    
    private int userId;
    private String name;
    private double age;
    private int gender;
    private String education;
    private String homeOwnership;

    private String email;
    private String oauthProvider;
    private String loginType;
}
