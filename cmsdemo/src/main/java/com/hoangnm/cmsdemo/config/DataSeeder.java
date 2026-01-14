package com.hoangnm.cmsdemo.config;

import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import com.hoangnm.cmsdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo Admin nếu chưa có
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }

        // Tạo User thường nếu chưa có
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }

        // Tạo vài sản phẩm mẫu
        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setName("iPhone 15 Pro Max");
            p1.setPrice(30000000.0);
            p1.setDescription("Điện thoại xịn nhất quả đất");
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("Samsung Galaxy S24");
            p2.setPrice(25000000.0);
            p2.setDescription("Đối thủ của iPhone");
            productRepository.save(p2);
        }
    }
}
