package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.Order;
import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.OrderRepository;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import com.hoangnm.cmsdemo.repository.UserRepository;
import com.hoangnm.cmsdemo.service.OrderSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final List<String> productCategories = List.of("Phone", "Headphone", "Watch", "Case", "Charger");

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("ordersTodayCount", orderRepository.countByOrderDateAfter(LocalDate.now().atStartOfDay()));
        return "admin/dashboard";
    }

    @GetMapping("/orders")
    public String listAllOrders(@RequestParam(value = "userId", required = false) Long userId,
                                @RequestParam(value = "orderDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate,
                                @RequestParam(value = "minPrice", required = false) Double minPrice,
                                @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                Model model) {
        
        if (minPrice != null && minPrice == 0) minPrice = null;
        if (maxPrice != null && maxPrice >= 100000000) maxPrice = null;

        Specification<Order> spec = OrderSpecification.filterBy(userId, orderDate, minPrice, maxPrice);
        List<Order> allOrders = orderRepository.findAll(spec);

        model.addAttribute("placedOrders", allOrders.stream().filter(o -> "Placed".equals(o.getStatus())).collect(Collectors.toList()));
        model.addAttribute("shippingOrders", allOrders.stream().filter(o -> "Shipping".equals(o.getStatus())).collect(Collectors.toList()));
        model.addAttribute("completedOrders", allOrders.stream().filter(o -> "Completed".equals(o.getStatus())).collect(Collectors.toList()));
        
        model.addAttribute("allUsers", userRepository.findAll());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("order", order);
        return "admin/order_detail";
    }

    @PostMapping("/orders/update-status/{id}")
    public String updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(value = "source", required = false) String source) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
             user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            userRepository.findById(user.getId()).ifPresent(existingUser -> user.setPassword(existingUser.getPassword()));
        }
        userRepository.save(user);
        if ("dashboard".equals(source)) { return "redirect:/admin/dashboard"; }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, @RequestParam(value = "source", required = false) String source, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword(""); 
        model.addAttribute("user", user);
        model.addAttribute("source", source);
        return "admin/user_form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, @RequestParam(value = "source", required = false) String source) {
        userRepository.deleteById(id);
        if ("dashboard".equals(source)) { return "redirect:/admin/dashboard"; }
        return "redirect:/admin/users";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/products";
    }
    
    @GetMapping("/products/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productCategories);
        return "admin/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, @RequestParam(value = "source", required = false) String source, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", productCategories);
        model.addAttribute("source", source);
        return "admin/product_form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, @RequestParam(value = "source", required = false) String source) {
        productRepository.deleteById(id);
        if ("dashboard".equals(source)) { return "redirect:/admin/dashboard"; }
        return "redirect:/admin/products";
    }
}
