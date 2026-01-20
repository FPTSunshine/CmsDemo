package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String root() {
        return "redirect:/user/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/user/home";
    }
    
    // HÀM GÂY XUNG ĐỘT ĐÃ BỊ XÓA

    @GetMapping("/category/{name}")
    public String viewCategory(@PathVariable("name") String name, Model model) {
        model.addAttribute("products", productRepository.findByCategory(name));
        model.addAttribute("categoryName", name);
        return "user/home";
    }
}
