package com.ssg_order.ssg_order_api.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pd_products_base")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_sn")
    private Long prdSn;

    @Column(name = "prd_no", nullable = false, unique = true)
    private String prdNo;

    @Column(name = "prd_name", nullable = false)
    private String prdName;

    @Column(name = "sale_price", nullable = false)
    private Long salePrice;

    @Column(name = "discount_price", nullable = false)
    private Long discountPrice;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updater", nullable = false)
    private String updater;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 재고 차감
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족: 현재=" + stock + ", 요청=" + quantity);
        }
        this.stock -= quantity;
    }

    // 재고 복원
    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.creator = "sujin3100";
        this.updater = "sujin3100";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updater = "sujin3100";
    }

    @Version
    @Column(name = "version")
    private Long version;
}
