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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final long OTP_VALID_DURATION = 5; // 5 minutes

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        if (userRepository.findByEmail(email).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No account found with that email address.");
            return "redirect:/forgot-password";
        }

        String otp = String.format("%06d", (int) (Math.random() * 1000000));
        String token = UUID.randomUUID().toString();

        userRepository.updateOtp(otp, LocalDateTime.now(), token, email);

        emailService.sendOtpEmail(email, otp);

        redirectAttributes.addFlashAttribute("message", "An OTP has been sent to your email.");
        return "redirect:/verify-otp?token=" + token;
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByResetToken(token).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid token.");
            return "redirect:/forgot-password";
        }
        
        model.addAttribute("token", token);
        model.addAttribute("email", user.getEmail()); // Thêm email vào model
        return "verify_otp";
    }

    @PostMapping("/verify-otp")
    public String handleVerifyOtp(@RequestParam("token") String token, @RequestParam("otp") String otp, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByResetToken(token).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid token. Please try again.");
            return "redirect:/forgot-password";
        }

        if (user.getOtpRequestedTime() != null) {
            long minutes = Duration.between(user.getOtpRequestedTime(), LocalDateTime.now()).toMinutes();
            if (minutes > OTP_VALID_DURATION) {
                redirectAttributes.addFlashAttribute("error", "OTP has expired. Please request a new one.");
                return "redirect:/forgot-password";
            }
        }

        if (!user.getOtp().equals(otp)) {
            redirectAttributes.addFlashAttribute("error", "Invalid OTP. Please try again.");
            return "redirect:/verify-otp?token=" + token;
        }

        return "redirect:/reset-password?token=" + token;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }

        User user = userRepository.findByResetToken(token).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/forgot-password";
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setOtp(null);
        user.setOtpRequestedTime(null);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "Your password has been reset successfully. Please login.");
        return "redirect:/login";
    }
}
