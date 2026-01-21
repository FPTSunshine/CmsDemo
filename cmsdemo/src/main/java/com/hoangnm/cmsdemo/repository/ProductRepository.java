package com.hoangnm.cmsdemo.repository;

import com.hoangnm.cmsdemo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    
    // Hàm phân trang cho category
    Page<Product> findByCategory(String category, Pageable pageable);

    // Hàm tìm kiếm theo tên (bị thiếu)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Hàm tìm kiếm theo tên có phân trang (thêm luôn để dùng sau này nếu cần)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
