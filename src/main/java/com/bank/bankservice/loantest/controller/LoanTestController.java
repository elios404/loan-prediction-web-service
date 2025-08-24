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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("loantest")
public class LoanTestController {

    @Autowired
    LoanService loanService;

    @Autowired
    LoanTestService loanTestService;

    // WebClient를 빌더 패턴으로 주입받아 사용
    @Autowired
    private WebClient.Builder webClientBuilder;

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

    /**
     * FastAPI 서버로 데이터를 보내고 결과를 받는 메서드.
     * WebClient를 사용하여 비동기적으로 HTTP POST 요청을 보냅니다.
     *
     * @param loanTest 폼에서 받은 LoanTest DTO 객체
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 리다이렉트할 뷰의 이름
     */
    @PostMapping("predict")
    public String predictLoanStatus(@ModelAttribute LoanTest loanTest, RedirectAttributes redirectAttributes) {
        try {
            // FastAPI 서버로 보낼 데이터를 준비 (폼에서 받은 DTO를 그대로 사용)
            LoanTest requestBody = loanTest;

            // WebClient 인스턴스 생성 및 기본 URL 설정
            WebClient client = webClientBuilder.baseUrl("http://127.0.0.1:8000").build();

            // FastAPI의 /predict_and_explain 엔드포인트에 POST 요청 전송
            // DTO 객체는 자동으로 JSON 형식으로 변환되어 전송됩니다.
            Map<String, Object> fastAPIResponse = client.post()
                    .uri("/predict_and_explain") // FastAPI의 실제 엔드포인트
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class) // FastAPI의 JSON 응답을 Map<String, Object>로 받음
                    .block(); // 동기적으로 요청을 완료하기 위해 사용

            // 응답 처리: 받은 응답을 로깅하고 뷰로 전달
            log.info("FastAPI 응답: " + fastAPIResponse);

            // 예측 결과와 SHAP 값을 flash attribute로 전달
            redirectAttributes.addFlashAttribute("predictionResult", fastAPIResponse.get("prediction"));
            redirectAttributes.addFlashAttribute("shapValues", fastAPIResponse.get("shap_values"));
            redirectAttributes.addFlashAttribute("message", "성공적으로 예측 결과를 받았습니다.");

        } catch (Exception e) {
            log.error("FastAPI 통신 실패", e);
            redirectAttributes.addFlashAttribute("message", "통신 실패: " + e.getMessage());
        }

        // 예측 결과를 보여주는 페이지로 리다이렉트
        return "redirect:/loantest/result";
    }

}
