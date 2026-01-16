package com.hoangnm.cmsdemo.api;

import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.model.Cart;
import com.hoangnm.cmsdemo.model.CartItem;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import com.hoangnm.cmsdemo.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam("productId") Long productId,
                                       @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                       HttpSession session) {
        
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Product not found!"));
        }

        Cart cart = cartService.getCart(session);
        cart.addItem(new CartItem(product, quantity));
        cartService.saveCart(session, cart);

        return ResponseEntity.ok(Map.of(
            "message", "Product added to cart!",
            "totalItems", cart.getTotalItems()
        ));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestParam("productId") Long productId,
                                        @RequestParam("quantity") int quantity,
                                        HttpSession session) {
        Cart cart = cartService.getCart(session);
        cart.updateItem(productId, quantity);
        cartService.saveCart(session, cart);

        CartItem updatedItem = cart.getItems().get(productId);
        double itemSubtotal = (updatedItem != null) ? updatedItem.getProduct().getPrice() * updatedItem.getQuantity() : 0;

        return ResponseEntity.ok(Map.of(
            "message", "Cart updated!",
            "totalItems", cart.getTotalItems(),
            "totalPrice", cart.getTotalPrice(),
            "itemSubtotal", itemSubtotal
        ));
    }
}
