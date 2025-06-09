package com.ssg_order.ssg_order_api.order.entity;

import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "od_order_dtl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_dtl_sn")
    private Long ordDtlSn;

    @Column(name = "ord_no", nullable = false)
    private String ordNo; // 주문번호

    @Column(name = "ord_dtl_no", nullable = false)
    private Integer ordDtlNo;

    @Column(name = "prd_no", nullable = false)
    private String prdNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ord_dtl_status", nullable = false)
    private OrderDtlStatus ordDtlStatus;

    @Column(name = "ord_fin_dtime")
    private LocalDateTime ordFinDtime;

    @Column(name = "ord_cncl_dtime")
    private LocalDateTime ordCnclDtime;

    @Column(name = "ord_qty", nullable = false)
    private Integer ordQty;

    @Column(name = "sale_price", nullable = false)
    private Long salePrice;

    @Column(name = "discount_price", nullable = false)
    private Long discountPrice;

    @Column(name = "pay_amt", nullable = false)
    private Long payAmt;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updater", nullable = false)
    private String updater;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ord_no", insertable = false, updatable = false)
    private Order order;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.ordFinDtime = now;
        this.creator = "sujin3100";
        this.updater = "sujin3100";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updater = "sujin3100";
    }
}
