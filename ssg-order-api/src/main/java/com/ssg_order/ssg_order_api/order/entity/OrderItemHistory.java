package com.ssg_order.ssg_order_api.order.entity;

import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "od_order_dtl_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_dtl_hist_sn")
    private Long ordDtlHistSn;

    @Column(name = "ord_dtl_sn", nullable = false)
    private Long ordDtlSn;

    @Column(name = "ord_no", nullable = false)
    private String ordNo;

    @Column(name = "ord_dtl_no", nullable = false)
    private Integer ordDtlNo;

    @Column(name = "prd_no", nullable = false)
    private Long prdNo;

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
    private Integer salePrice;

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;

    @Column(name = "pay_amt", nullable = false)
    private Integer payAmt;

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
    public void onCreate() {
        this.histCreatedAt = LocalDateTime.now();
        this.histCreator = "sujin3100";
    }
}
