package com.hoangnm.cmsdemo.repository;

import com.hoangnm.cmsdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByResetToken(String token);

    // Đổi tên hàm thành updateOtp và thêm token vào
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.otp = ?1, u.otpRequestedTime = ?2, u.resetToken = ?3 WHERE u.email = ?4")
    void updateOtp(String otp, LocalDateTime otpRequestedTime, String token, String email);
}
