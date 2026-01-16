package com.hoangnm.cmsdemo.repository;

import com.hoangnm.cmsdemo.entity.Order;
import com.hoangnm.cmsdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUser(User user);
    long countByOrderDateAfter(LocalDateTime date);
}
