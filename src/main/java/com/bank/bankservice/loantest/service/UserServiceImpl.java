package com.bank.bankservice.loantest.service;

import com.bank.bankservice.loantest.dao.UserRepository;
import com.bank.bankservice.loantest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public int getUserCount() {
        return userRepository.getUserCount();
    }

    @Override
    public int getUserCount(int userId) {
        return userRepository.getUserCount(userId);
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userRepository.getUserList();
        return userList;
    }

    @Override
    public User getUserInfo(int userId) {
        User user = userRepository.getUserInfo(userId);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void insertUser(User user) {
        userRepository.insertUser(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return userRepository.deleteUser(id);
    }
}
