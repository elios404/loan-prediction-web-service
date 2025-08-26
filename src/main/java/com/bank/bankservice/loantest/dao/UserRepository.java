package com.bank.bankservice.loantest.dao;

import com.bank.bankservice.loantest.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {
    
    // DB에서 user table에 접근하기 위한 repository, UserMapeer.xml에 구현됨
    
    int getUserCount();
    int getUserCount(@Param("userId") int userId);

    List<User> getUserList(); // 전체 유저의 목록을 User 객체의 리스트로 반환
    User getUserInfo(int userId); // 특정 유저의 세부 정보를 User 객체로 반환

    void insertUser(User user); // 새로운 유저 정보를 insert를 통해서 등록
    void updateUser(User user); // 기존 유저의 정보를 update를 통해서 갱신, 각 유저의 details 페이지에서 갱신 가능
    int deleteUser(int id); // 기존 유저의 정보를 삭제, 각 유저의 details 페이지에서 삭제 가능

    User findByEmail(@Param("email") String email);
}
