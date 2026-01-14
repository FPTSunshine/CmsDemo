package com.hoangnm.cmsdemo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // Tránh dùng tên 'user' vì có thể trùng từ khóa SQL
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Sẽ lưu BCrypt Hash

    @Column(nullable = false)
    private String role; // Ví dụ: "ROLE_ADMIN", "ROLE_USER"
}
