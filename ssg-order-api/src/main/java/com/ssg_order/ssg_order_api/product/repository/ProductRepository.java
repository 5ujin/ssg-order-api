package com.ssg_order.ssg_order_api.product.repository;

import com.ssg_order.ssg_order_api.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByPrdNo(String productNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.prdNo = :prdNo")
    Optional<Product> findByPrdNoForUpdate(@Param("prdNo") String prdNo);
}
