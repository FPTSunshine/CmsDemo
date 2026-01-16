package com.hoangnm.cmsdemo.repository;

import com.hoangnm.cmsdemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm sản phẩm có tên chứa từ khóa (không phân biệt hoa thường)
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    // Tìm sản phẩm theo danh mục
    List<Product> findByCategory(String category);
}
