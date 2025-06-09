package com.ssg_order.ssg_order_api.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pd_products_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_history_sn")
    private Long prdHistorySn;

    @Column(name = "prd_sn", nullable = false)
    private Long prdSn;

    @Column(name = "prd_no", nullable = false)
    private String prdNo;

    @Column(name = "prd_name", nullable = false)
    private String prdName;

    @Column(name = "sale_price", nullable = false)
    private Long salePrice;

    @Column(name = "discount_price", nullable = false)
    private Long discountPrice;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updater", nullable = false)
    private String updater;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "hist_creator", nullable = false)
    private String histCreator;

    @Column(name = "hist_created_at", nullable = false)
    private LocalDateTime histCreatedAt;

    @PrePersist
    public void onPersist() {
        this.histCreatedAt = LocalDateTime.now();
        if (this.histCreator == null) this.histCreator = "sujin3100";
    }
}