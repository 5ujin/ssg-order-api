package com.ssg_order.ssg_order_api.order.entity;

import com.ssg_order.ssg_order_api.order.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "od_order_base")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_sn")
    private int ordSn;

    @Column(name = "ord_no", nullable = false, unique = true)
    private String ordNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ord_status", nullable = false)
    private OrderStatus ordStatus;

    @Column(name = "ord_fin_dtime")
    private LocalDateTime ordFinDtime;

    @Column(name = "ord_cncl_dtime")
    private LocalDateTime ordCnclDtime;

    @Column(name = "total_pay_amt")
    private Long totalPayAmt;

    @Column(name = "cancelable_amt")
    private Long cancelableAmt;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "creator", nullable = false)
    private String creator;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updater", nullable = false)
    private String updater;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.ordFinDtime = now;
        this.creator = "sujin3100";
        this.updater = "sujin3100";
        this.userId = "sujin3100";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updater = "sujin3100";
    }
}
