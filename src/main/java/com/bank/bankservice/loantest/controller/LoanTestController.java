package com.bank.bankservice.loantest.controller;

import com.bank.bankservice.loantest.dto.LoanTest;
import com.bank.bankservice.loantest.model.Loan;
import com.bank.bankservice.loantest.model.User;
import com.bank.bankservice.loantest.service.LoanService;
import com.bank.bankservice.loantest.service.LoanTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("loantest")
public class LoanTestController {

    @Autowired
    LoanService loanService;

    @Autowired
    LoanTestService loanTestService;

    @GetMapping("list")
    public String getAllTests(Model model) {

        List<LoanTest> testList = loanTestService.getTestList();

        model.addAttribute("testList", testList);
        return "loantest/list";
    }

    @GetMapping("details/{userId}/{loanId}")
    public String getTestInfo(@PathVariable("userId") int userId, @PathVariable("loanId") int loanId, Model model) {

        LoanTest loanTest = loanTestService.getTestInfo(userId, loanId);
        model.addAttribute("testInfo", loanTest);
        return "loantest/details";
    }

    @GetMapping("insert")
    public String insert(Model model) {

        model.addAttribute("loanTest", new LoanTest()); // 폼에 담아서 보내기 위해

        return "loantest/insert";
    }

    @PostMapping("insert")
    public String insertTest(Loan loan, RedirectAttributes redirectAttributes) {
        log.info("test insert" + loan.toString());
        try {
            loanService.insertLoan(loan);
            redirectAttributes.addFlashAttribute("message", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "fail" + e.getMessage());
        }

        return "redirect:/loantest/list";
    }

    @GetMapping("update/{userId}/{loanId}")
    public String update(@PathVariable("userId") int userId, @PathVariable("loanId") int loanId, Model model) {

        LoanTest loanTest = loanTestService.getTestInfo(userId, loanId);

        model.addAttribute("loanTest", loanTest); // 폼에 담아서 보내기 위해

        return "loantest/update";
    }

    @PostMapping("update")
    public String updateUser(Loan loan, RedirectAttributes redirectAttributes) {
        log.info("test update" + loan.toString());
        try {
            loanService.updateLoan(loan);
            redirectAttributes.addFlashAttribute("message", "success");
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "fail" + e.getMessage());
        }

        // "userId"라는 이름의 파라미터에 user.getUserId() 값을 할당
        redirectAttributes.addAttribute("loanId", loan.getLoanId());

        return "redirect:/loantest/details/{userId}/{loanId}";
    }

    @GetMapping("delete/{userId}/{loanId}")
    public String deleteUser(@PathVariable("userId") int userId, @PathVariable("loanId") int loanId, RedirectAttributes redirectAttributes) {
        log.info("test delete" + loanId);
        try {
            int cnt = loanService.deleteLoan(userId, loanId);

            if (cnt > 0) {
                redirectAttributes.addFlashAttribute("message", "success");
            }  else {
                redirectAttributes.addFlashAttribute("message", "fail" + cnt);
            }
        }   catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "fail" + e.getMessage());
        }

        return "redirect:/loantest/list";
    }


}
