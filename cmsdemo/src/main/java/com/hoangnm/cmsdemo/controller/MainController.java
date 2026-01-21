package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    // Sửa lại hàm root để chuyển hướng thông minh
    @GetMapping("/")
    public String root(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
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
    
    @GetMapping("/user/home")
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page, 10));
        
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "user/home";
    }

    @GetMapping("/category/{name}")
    public String viewCategory(@PathVariable("name") String name, Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Product> productPage = productRepository.findByCategory(name, PageRequest.of(page, 10));
        
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categoryName", name);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "user/home";
    }
}
