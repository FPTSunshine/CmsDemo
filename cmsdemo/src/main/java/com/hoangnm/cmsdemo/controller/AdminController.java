package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import com.hoangnm.cmsdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("productCount", productRepository.count());
        return "admin/dashboard";
    }

    // ==========================================
    // USER MANAGEMENT
    // ==========================================
    
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, 
                           @RequestParam(value = "source", required = false) String source) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
             user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        userRepository.save(user);
        
        // Điều hướng dựa trên nguồn
        if ("dashboard".equals(source)) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, 
                                   @RequestParam(value = "source", required = false) String source,
                                   Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword(""); 
        model.addAttribute("user", user);
        model.addAttribute("source", source); // Truyền source sang view để giữ lại
        return "admin/user_form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @RequestParam(value = "source", required = false) String source) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        
        if ("dashboard".equals(source)) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/users";
    }

    // ==========================================
    // PRODUCT MANAGEMENT
    // ==========================================

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product, 
                              @RequestParam("image") MultipartFile file,
                              @RequestParam(value = "source", required = false) String source) throws IOException {
        
        if (!file.isEmpty()) {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            product.setImageUrl("/uploads/" + fileNames.toString());
        }

        productRepository.save(product);
        
        if ("dashboard".equals(source)) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, 
                                      @RequestParam(value = "source", required = false) String source,
                                      Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("source", source);
        return "admin/product_form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id,
                                @RequestParam(value = "source", required = false) String source) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        productRepository.delete(product);
        
        if ("dashboard".equals(source)) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/products";
    }
}
