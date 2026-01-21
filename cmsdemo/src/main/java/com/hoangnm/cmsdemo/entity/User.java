package com.hoangnm.cmsdemo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // Các trường cho chức năng "Quên mật khẩu"
    private String resetToken;
    private String otp;
    private LocalDateTime otpRequestedTime;

    // Các trường cho chức năng OAuth2
    private boolean isOAuth2User;
    private String tempOAuth2Email;
}
