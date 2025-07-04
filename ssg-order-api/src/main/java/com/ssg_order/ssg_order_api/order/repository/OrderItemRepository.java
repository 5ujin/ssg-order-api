package com.ssg_order.ssg_order_api.order.repository;

import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Optional<OrderItem> findByOrdNoAndPrdNo(String ordNo, String prdNo);

    List<OrderItem> findAllByOrdNo(String ordNo);
}
