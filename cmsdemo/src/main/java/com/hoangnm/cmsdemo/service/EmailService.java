package com.hoangnm.cmsdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Hello,\n\nYour One-Time Password (OTP) for password reset is: " + otp + "\n\nThis OTP is valid for 5 minutes.\n\nRegards,\nCMS Demo Team");
        
        mailSender.send(message);
    }
}
