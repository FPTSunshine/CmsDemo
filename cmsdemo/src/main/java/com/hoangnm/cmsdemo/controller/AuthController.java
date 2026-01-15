package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository;
import com.hoangnm.cmsdemo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Controller
public class AuthController {

    private static final long OTP_VALID_DURATION = 5; // 5 phút

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        if (userRepository.findByEmail(email).isEmpty()) {
            model.addAttribute("error", "Email not found!");
            return "forgot_password";
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        
        // SỬA LỖI: Chỉ update các trường cần thiết, không đụng đến password
        userRepository.updateOtp(otp, LocalDateTime.now(), email);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            model.addAttribute("error", "Error sending email. Check your credentials in application.properties.");
            e.printStackTrace();
            return "forgot_password";
        }

        return "redirect:/verify-otp?email=" + email;
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify_otp";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty() || userOptional.get().getOtp() == null || !userOptional.get().getOtp().equals(otp)) {
            model.addAttribute("error", "Invalid OTP!");
            model.addAttribute("email", email);
            return "verify_otp";
        }

        User user = userOptional.get();
        if (user.getOtpRequestedTime().plusMinutes(OTP_VALID_DURATION).isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "OTP has expired!");
            model.addAttribute("email", email);
            return "verify_otp";
        }

        return "redirect:/reset-password?otp=" + otp;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("otp") String otp, Model model) {
        Optional<User> userOptional = userRepository.findByOtp(otp);
        if (userOptional.isEmpty()) {
            return "redirect:/forgot-password?error=Invalid+session";
        }
        model.addAttribute("otp", otp);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("otp") String otp, @RequestParam("password") String password, Model model) {
        Optional<User> userOptional = userRepository.findByOtp(otp);
        if (userOptional.isEmpty()) {
            return "redirect:/forgot-password?error=Invalid+session";
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(password));
        user.setOtp(null);
        user.setOtpRequestedTime(null);
        userRepository.save(user);

        return "redirect:/login?message=Password+changed+successfully";
    }
}
