package com.bank.bankservice.loantest.controller;

import com.bank.bankservice.loantest.model.User;
import com.bank.bankservice.loantest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("user") // 처음에 RESTful API 로 구현하고자 함. 그러나 단순 MVC로 전환
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("list")
    public String getAllUsers(Model model) {

        List<User> userList = userService.getUserList();

        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("details/{userId}")
    public String getUserInfo(@PathVariable("userId") int userId, Model model) {
        User user = userService.getUserInfo(userId);
        model.addAttribute("user", user);
        return "user/details";
    }

    @GetMapping("insert")
    public String insert(Model model) {

        model.addAttribute("user", new User()); // 폼에 담아서 보내기 위해

        return "user/insert";
    }

    @PostMapping("insert")
    public String insertUser(User user, RedirectAttributes redirectAttributes) {
        log.info("user insert" + user.toString());
        try {
            userService.insertUser(user);
            redirectAttributes.addFlashAttribute("message", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "fail" + e.getMessage());
        }

        return "redirect:/user/list";
    }

    @GetMapping("update/{userId}")
    public String update(@PathVariable("userId") int userId, Model model) {

        User user = userService.getUserInfo(userId);

        model.addAttribute("user", user); // 폼에 담아서 보내기 위해

        return "user/update";
    }

    @PostMapping("update")
    public String updateUser(User user, RedirectAttributes redirectAttributes) {
        log.info("user update" + user.toString());
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "success");
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "fail" + e.getMessage());
        }

        // "userId"라는 이름의 파라미터에 user.getUserId() 값을 할당
        redirectAttributes.addAttribute("userId", user.getUserId());

        return "redirect:/user/details/{userId}";
    }

    @GetMapping("delete/{userId}")
    public String deleteUser(@PathVariable("userId") int userId, RedirectAttributes redirectAttributes) {
        log.info("user delete" + userId);
        try {
            int cnt = userService.deleteUser(userId);

            if (cnt > 0) {
                redirectAttributes.addFlashAttribute("message", "success");
            }  else {
                redirectAttributes.addFlashAttribute("message", "fail");
            }
        }   catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "fail" + e.getMessage());
        }

        return "redirect:/user/list";
    }



}
