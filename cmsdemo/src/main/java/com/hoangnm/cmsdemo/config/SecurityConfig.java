package com.hoangnm.cmsdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Cho phép truy cập tài nguyên tĩnh
                .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ ADMIN mới vào được /admin/**
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER và ADMIN đều vào được /user/**
                .anyRequest().authenticated() // Các trang khác bắt buộc phải đăng nhập
            )
            .formLogin((form) -> form
                .loginPage("/login") // Trang login tùy chỉnh (mình sẽ tạo sau)
                .defaultSuccessUrl("/default", true) // Đăng nhập thành công thì chuyển hướng vào đây để xử lý tiếp
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
