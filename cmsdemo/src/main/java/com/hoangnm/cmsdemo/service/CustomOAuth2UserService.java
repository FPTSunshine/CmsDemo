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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = (String) oAuth2User.getAttributes().get("email");
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        User user;
        boolean needsToCompleteRegistration = false;

        if (userOptional.isPresent()) {
            // User đã tồn tại
            user = userOptional.get();
            // Kiểm tra xem đây có phải là tài khoản tạm do bỏ dở đăng ký không
            if (user.isOAuth2User()) {
                needsToCompleteRegistration = true;
            }
        } else {
            // User hoàn toàn mới
            user = new User();
            user.setEmail(email); // Dùng email thật
            user.setOAuth2User(true); // Đánh dấu là cần hoàn tất đăng ký
            user.setUsername("oauth2_temp_" + UUID.randomUUID()); // Username tạm
            user.setPassword(""); // Password tạm
            user.setRole("ROLE_USER");
            userRepository.save(user);
            needsToCompleteRegistration = true;
        }

        Map<String, Object> mutableAttributes = new HashMap<>(oAuth2User.getAttributes());
        mutableAttributes.put("needsToCompleteRegistration", needsToCompleteRegistration);
        mutableAttributes.put("email", user.getEmail()); // Luôn đảm bảo có email

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new DefaultOAuth2User(authorities, mutableAttributes, "email");
    }
}
