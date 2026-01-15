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
    Optional<User> findByOtp(String otp);

    @Transactional
    @Modifying
    @Query("update User u set u.otp = ?1, u.otpRequestedTime = ?2 where u.email = ?3")
    void updateOtp(String otp, LocalDateTime otpRequestedTime, String email);
}
