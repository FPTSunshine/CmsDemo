package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.UserRepository; // Thay thế UserService
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Sử dụng UserRepository

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/complete-registration")
    public String showCompleteRegistrationForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("OAUTH2_USER_EMAIL");
        if (email == null) {
            return "redirect:/login";
        }
        User user = new User();
        user.setEmail(email);
        model.addAttribute("user", user);
        return "complete_registration";
    }

    @PostMapping("/complete-registration")
    public String completeRegistration(@RequestParam String email,
                                       @RequestParam String username,
                                       @RequestParam String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user); // Sửa thành userRepository.save()
        return "redirect:/login?registration_success";
    }
}
