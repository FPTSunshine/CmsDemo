package com.hoangnm.cmsdemo.repository;

import com.hoangnm.cmsdemo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
