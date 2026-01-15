package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Hiển thị form nhập Email
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    // 2. Xử lý gửi Token
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Email not found!");
            return "forgot_password";
        }

        User user = userOptional.get();
        // Tạo token ngẫu nhiên
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // GIẢ LẬP GỬI MAIL: In ra console
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        System.out.println("==========================================");
        System.out.println("EMAIL SENT TO: " + email);
        System.out.println("RESET LINK: " + resetLink);
        System.out.println("==========================================");

        model.addAttribute("message", "We have sent a reset link to your email. Please check console for demo link.");
        return "forgot_password";
    }

    // 3. Hiển thị form nhập mật khẩu mới (khi bấm vào link)
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Invalid Token!");
            return "login"; // Hoặc trang lỗi
        }

        model.addAttribute("token", token);
        return "reset_password";
    }

    // 4. Xử lý đổi mật khẩu
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token, 
                                       @RequestParam("password") String password, Model model) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Invalid Token!");
            return "login";
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(password)); // Mã hóa pass mới
        user.setResetToken(null); // Xóa token sau khi dùng xong
        userRepository.save(user);

        return "redirect:/login?message=Password+changed+successfully";
    }
}
