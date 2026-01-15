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
    
    @Column(unique = true) // Email nên là duy nhất
    private String email;
    
    // OTP
    private String otp;
    private LocalDateTime otpRequestedTime;
}
