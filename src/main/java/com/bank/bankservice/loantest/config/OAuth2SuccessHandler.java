package com.bank.bankservice.loantest.config;

import com.bank.bankservice.loantest.model.User;
import com.bank.bankservice.loantest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// 새로운 import 추가!
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Component
public class    OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = null;
        String name = null;
        String provider = null;


        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();


        if ("google".equals(registrationId)) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
            provider = "google";
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverResponse = oauth2User.getAttribute("response");
            email = (String) naverResponse.get("email");
            name = (String) naverResponse.get("name");
            provider = "naver";
        } else if ("facebook".equals(registrationId)) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
            provider = "facebook";
        }


        User existingUser = userService.findByEmail(email);

        HttpSession session = request.getSession();

        if (existingUser != null) {

            response.sendRedirect("http://localhost:5173");
        } else {
            // 새 사용자 - 회원가입 필요
            session.setAttribute("oauthEmail", email);
            session.setAttribute("oauthName", name);
            session.setAttribute("oauthProvider", provider);
            response.sendRedirect("http://localhost:5173");
        }
    }
}