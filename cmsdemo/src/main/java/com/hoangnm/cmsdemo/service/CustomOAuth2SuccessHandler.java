package com.hoangnm.cmsdemo.service;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            super.setDefaultTargetUrl("/default");
        } else {
            request.getSession().setAttribute("OAUTH2_USER_EMAIL", email);
            super.setDefaultTargetUrl("/complete-registration");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
