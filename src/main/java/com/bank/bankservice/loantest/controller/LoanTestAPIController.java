package com.bank.bankservice.loantest.controller;

import com.bank.bankservice.loantest.dto.LoanTest;
import com.bank.bankservice.loantest.model.Loan;
import com.bank.bankservice.loantest.model.User;
import com.bank.bankservice.loantest.service.LoanService;
import com.bank.bankservice.loantest.service.LoanTestService;
import com.bank.bankservice.loantest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("api/loantest")
public class LoanTestAPIController {

    @Autowired
    LoanService loanService;

    @Autowired
    LoanTestService loanTestService;

    // 사용자 정보 조회를 위한 UserService 추가
    @Autowired
    UserService userService;

    // WebClient를 빌더 패턴으로 주입받아 사용
    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("homepage")
    public String getHomepage(Model model) {
        return "com/base/homepage";
    }

    /**
     * 전체 대출 테스트 목록을 조회하여 리스트 페이지로 반환합니다.
     * @param model 뷰로 전달할 데이터를 담는 객체
     * @return 뷰 이름
     */
    @GetMapping("list")
    public String getAllTests(Model model) {
        List<LoanTest> testList = loanTestService.getTestList();
        model.addAttribute("testList", testList);
        return "com/loantest/list";
    }

    /**
     * 특정 대출 테스트 상세 정보를 조회하여 상세 페이지로 반환합니다.
     * @param userId 유저 ID
     * @param loanId 대출 ID
     * @param model 뷰로 전달할 데이터를 담는 객체
     * @return 뷰 이름
     */
    @GetMapping("details/{userId}/{loanId}")
    public String getTestInfo(@PathVariable("userId") int userId, @PathVariable("loanId") int loanId, Model model) {
        LoanTest loanTest = loanTestService.getTestInfo(userId, loanId);
        model.addAttribute("testInfo", loanTest);
        return "com/loantest/details";
    }

    /**
     * 새로운 대출 평가를 위한 입력 폼 페이지를 반환합니다.
     * @param model 뷰로 전달할 데이터를 담는 객체
     * @return 뷰 이름
     */
    @GetMapping("insert")
    public String insert(Model model) {
        model.addAttribute("loanTest", new LoanTest()); // 폼에 담아서 보내기 위해
        return "com/loantest/insert";
    }

    /**
     * 폼에서 전송된 데이터를 바탕으로 대출 상태를 예측하고 DB에 저장합니다.
     *
     * @param loanTest 폼에서 받은 LoanTest DTO 객체 (일부 필드만 채워져 있음)
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 리다이렉트할 뷰의 이름
     */
    @PostMapping("insert")
    public String predictAndInsertTest(@ModelAttribute LoanTest loanTest, RedirectAttributes redirectAttributes) {
        log.info("대출 평가 예측 및 등록 시작: " + loanTest.toString());

        try {
            // 1. 폼에서 받은 userId를 사용하여 누락된 사용자 정보를 조회
            User user = userService.getUserInfo(loanTest.getUserId());

            // 조회된 정보가 없으면 예외 처리
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "fail: 사용자 정보를 찾을 수 없습니다.");
                return "redirect:/loantest/insert";
            }

            // 2. 폼에서 받은 금융 정보와 조회한 유저 정보를 결합하여 완전한 객체 생성
            // 폼에서 넘어온 loanTest 객체에 조회한 사용자 정보를 추가로 설정
            loanTest.setName(user.getName());
            loanTest.setAge(user.getAge());
            loanTest.setGender(user.getGender());
            loanTest.setEducation(user.getEducation());
            loanTest.setHomeOwnership(user.getHomeOwnership());

            // 3. WebClient를 사용하여 FastAPI 서버로 예측 요청
            WebClient client = webClientBuilder.baseUrl("http://127.0.0.1:8000").build();

            // FastAPI의 /predict_and_explain 엔드포인트에 POST 요청 전송
            // DTO 객체는 자동으로 JSON 형식으로 변환되어 전송됩니다.
            Map<String, Object> fastAPIResponse = client.post()
                    .uri("/predict_and_explain") // FastAPI의 실제 엔드포인트
                    .bodyValue(loanTest)
                    .retrieve()
                    .bodyToMono(Map.class) // FastAPI의 JSON 응답을 Map으로 받음
                    .block(); // 동기적으로 요청을 완료하기 위해 사용

            // 4. 응답 처리 및 예측 결과 반영
            int predictedLoanStatus = ((Number) fastAPIResponse.get("prediction")).intValue();
            List<Double> loanExplain = (List<Double>) fastAPIResponse.get("shap_values");

//            loanTest.setLoanStatus(predictedLoanStatus); // 예측된 대출 상태를 객체에 설정

            // 5. LoanTest 객체를 Loan 객체로 변환하여 DB에 저장
            Loan loan = new Loan();
            BeanUtils.copyProperties(loanTest, loan); // LoanTest의 속성을 Loan으로 복사

            // FastAPI에서 받은 SHAP 값을 Loan 객체의 loanExplain 필드에 설정
            loan.setLoanStatus(predictedLoanStatus);
            loan.setLoanExplain(loanExplain);

            loanService.insertLoan(loan);

            log.info("최종적으로 DB에 저장될 대출 정보: " + loan.toString());
            redirectAttributes.addFlashAttribute("message", "예측된 대출 상태(" + predictedLoanStatus + ")가 성공적으로 등록되었습니다.");

        } catch (Exception e) {
            log.error("대출 예측 및 등록 실패", e);
            redirectAttributes.addFlashAttribute("message", "통신 또는 등록 실패: " + e.getMessage());
        }

        return "redirect:/loantest/list";
    }


    /**
     * 대출 정보 업데이트를 위한 폼 페이지를 반환합니다.
     * @param userId 유저 ID
     * @param loanId 대출 ID
     * @param model 뷰로 전달할 데이터를 담는 객체
     * @return 뷰 이름
     */
    @GetMapping("update/{userId}/{loanId}")
    public String update(@PathVariable("userId") int userId, @PathVariable("loanId") int loanId, Model model) {
        LoanTest loanTest = loanTestService.getTestInfo(userId, loanId);
        model.addAttribute("loanTest", loanTest); // 폼에 담아서 보내기 위해
        return "com/loantest/update";
    }

    /**
     * 폼에서 전송된 데이터로 대출 정보를 업데이트합니다.
     * @param loan 업데이트할 대출 정보를 담는 객체
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 리다이렉트할 뷰의 이름
     */
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

    /**
     * 대출 정보를 삭제합니다.
     * @param userId 유저 ID
     * @param loanId 대출 ID
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 리다이렉트할 뷰의 이름
     */
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
