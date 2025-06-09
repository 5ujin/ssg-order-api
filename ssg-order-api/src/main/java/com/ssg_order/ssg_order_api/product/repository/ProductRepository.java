package com.ssg_order.ssg_order_api.product.repository;

import com.ssg_order.ssg_order_api.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByPrdNo(String productNo);
}
