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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
    
    @Column(unique = true)
    private String email;
    
    // OTP
    private String otp;
    private LocalDateTime otpRequestedTime;

    // OAuth2 specific fields
    private boolean isOAuth2User = false; // True if user logged in via OAuth2 and needs to complete registration
    private String tempOAuth2Email; // Temporarily store email for OAuth2 users
}
