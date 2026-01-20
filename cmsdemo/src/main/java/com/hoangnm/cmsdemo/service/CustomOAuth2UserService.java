package com.hoangnm.cmsdemo.service;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (userOptional.isPresent()) {
            // Nếu user đã tồn tại trong DB, lấy role của họ
            User user = userOptional.get();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
        } else {
            // Nếu user chưa tồn tại, gán role mặc định nhưng KHÔNG LƯU vào DB
            // Việc lưu vào DB sẽ do trang Complete Registration đảm nhận
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
    }
}
