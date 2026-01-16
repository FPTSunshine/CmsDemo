package com.hoangnm.cmsdemo.service;

import com.hoangnm.cmsdemo.model.Cart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private static final String CART_SESSION_KEY = "shoppingCart";

    // Lấy giỏ hàng từ session, nếu chưa có thì tạo mới
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    // Lưu lại giỏ hàng vào session
    public void saveCart(HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }
}
