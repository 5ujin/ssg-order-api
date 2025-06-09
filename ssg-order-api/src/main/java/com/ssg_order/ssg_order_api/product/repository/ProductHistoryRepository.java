package com.ssg_order.ssg_order_api.product.repository;

import com.ssg_order.ssg_order_api.product.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, String> {
}
