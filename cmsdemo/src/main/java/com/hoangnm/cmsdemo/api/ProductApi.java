package com.hoangnm.cmsdemo.api;

import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Quan trọng: Trả về JSON
@RequestMapping("/api/products")
public class ProductApi {

    @Autowired
    private ProductRepository productRepository;

    // API: GET /api/products/search?keyword=iphone
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("keyword") String keyword) {
        List<Product> results = productRepository.findByNameContainingIgnoreCase(keyword);
        return ResponseEntity.ok(results);
    }
    
    // API: GET /api/products (Lấy tất cả)
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}
