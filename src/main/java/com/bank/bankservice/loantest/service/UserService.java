package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    int getUserCount();
    int getUserCount(@Param("userId") int userId);

    List<User> getUserList();
    User getUserInfo(int userId);

    void insertUser(User user);
    void updateUser(User user);
    int deleteUser(int id);

    User findByEmail(String email);
}
