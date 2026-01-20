package com.hoangnm.cmsdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString // Thêm lại annotation này để log in ra nội dung
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    private String category;
}
