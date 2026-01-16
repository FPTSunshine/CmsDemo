package com.hoangnm.cmsdemo.controller;

import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.model.Cart;
import com.hoangnm.cmsdemo.model.CartItem;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import com.hoangnm.cmsdemo.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        return "user/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId, 
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity, 
                            HttpSession session,
                            HttpServletRequest request) { // Thêm HttpServletRequest
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        Cart cart = cartService.getCart(session);
        cart.addItem(new CartItem(product, quantity));
        cartService.saveCart(session, cart);

        // Lấy URL của trang trước đó và redirect về
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/user/home");
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") Long productId, HttpSession session) {
        Cart cart = cartService.getCart(session);
        cart.removeItem(productId);
        cartService.saveCart(session, cart);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") Long productId, 
                             @RequestParam("quantity") int quantity, 
                             HttpSession session) {
        Cart cart = cartService.getCart(session);
        cart.updateItem(productId, quantity);
        cartService.saveCart(session, cart);
        return "redirect:/cart";
    }
}
