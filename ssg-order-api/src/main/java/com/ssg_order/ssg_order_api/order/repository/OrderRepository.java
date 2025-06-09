package com.ssg_order.ssg_order_api.order.repository;

import com.ssg_order.ssg_order_api.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository  extends JpaRepository<Order, String> {
    boolean existsByOrdNo(String orderNo);

    Optional<Order> findByOrdNo(String ordNo);
}
