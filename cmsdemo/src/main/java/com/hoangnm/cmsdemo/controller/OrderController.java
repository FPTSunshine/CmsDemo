package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.Order;
import com.hoangnm.cmsdemo.entity.OrderDetail;
import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.model.Cart;
import com.hoangnm.cmsdemo.repository.OrderRepository;
import com.hoangnm.cmsdemo.repository.UserRepository;
import com.hoangnm.cmsdemo.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final double MAX_ORDER_TOTAL = 100000000.0;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/create")
    public String createOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        Cart cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/cart";
        }

        if (cart.getTotalPrice() > MAX_ORDER_TOTAL) {
            redirectAttributes.addFlashAttribute("error", "Order total cannot exceed 100,000,000 VND!");
            return "redirect:/cart";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("Placed"); // Dịch sang Tiếng Anh

        cart.getItems().values().forEach(cartItem -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(cartItem.getProduct());
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(cartItem.getProduct().getPrice());
            order.getOrderDetails().add(detail);
        });

        orderRepository.save(order);
        cart.clear();
        cartService.saveCart(session, cart);

        redirectAttributes.addFlashAttribute("message", "Order placed successfully!");
        return "redirect:/orders";
    }

    @GetMapping
    public String listOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("orders", orderRepository.findByUser(user));
        return "user/orders";
    }

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!order.getUser().getUsername().equals(currentUsername)) {
            return "redirect:/orders?error=Access denied";
        }

        model.addAttribute("order", order);
        return "user/order_detail";
    }
}
