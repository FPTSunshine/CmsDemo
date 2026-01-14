package com.hoangnm.cmsdemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Trả về file login.html
    }

    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/user/home";
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Vào trang chủ thì đẩy về login
    }
}
