package com.hoangnm.cmsdemo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        
        // Dùng cờ mới, rõ ràng hơn
        boolean needsToComplete = (boolean) oauthUser.getAttributes().getOrDefault("needsToCompleteRegistration", false);

        if (needsToComplete) {
            String email = (String) oauthUser.getAttributes().get("email");
            response.sendRedirect("/complete-registration?email=" + email);
        } else {
            response.sendRedirect("/default");
        }
    }
}
