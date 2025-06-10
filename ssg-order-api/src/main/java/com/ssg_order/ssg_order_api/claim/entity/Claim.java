package com.ssg_order.ssg_order_api.claim.entity;

import com.ssg_order.ssg_order_api.claim.model.ClaimReason;
import com.ssg_order.ssg_order_api.claim.model.ClaimStatus;
import com.ssg_order.ssg_order_api.claim.model.ClaimType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "or_claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_sn")
    private Long claimSn;

    @Column(name = "claim_no", nullable = false)
    private String claimNo;

    @Column(name = "ord_no", nullable = false)
    private String ordNo;

    @Column(name = "ord_dtl_no", nullable = false)
    private Integer ordDtlNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    private ClaimType claimType;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status", nullable = false)
    private ClaimStatus claimStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_reason")
    private ClaimReason claimReason;

    @Column(name = "prd_no", nullable = false)
    private String prdNo;

    @Column(name = "claim_qty", nullable = false)
    private Integer claimQty;

    @Column(name = "claim_dtime")
    private LocalDateTime claimDtime;

    @Column(name = "claim_fin_dtime")
    private LocalDateTime claimFinDtime;

    @Column(name = "refund_amt")
    private Long refundAmt;

    @Column(name = "creator", nullable = false, updatable = false)
    private String creator;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updater", nullable = false)
    private String updater;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.creator = "sujin3100";
        this.updater = "sujin3100";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updater = "sujin3100";
        this.updatedAt = LocalDateTime.now();
    }
}
