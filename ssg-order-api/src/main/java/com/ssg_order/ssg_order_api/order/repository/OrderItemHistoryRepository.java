package com.ssg_order.ssg_order_api.order.repository;

import com.ssg_order.ssg_order_api.order.entity.OrderItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemHistoryRepository extends JpaRepository<OrderItemHistory, String> {
}
