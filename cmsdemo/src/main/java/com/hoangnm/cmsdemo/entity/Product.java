package com.hoangnm.cmsdemo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    @Column(length = 1000)
    private String description;

    private String imageUrl; // Lưu đường dẫn ảnh
}
