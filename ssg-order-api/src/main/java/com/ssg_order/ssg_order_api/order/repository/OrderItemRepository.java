package com.ssg_order.ssg_order_api.order.repository;

import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
