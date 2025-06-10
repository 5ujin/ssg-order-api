package com.ssg_order.ssg_order_api.claim.repository;

import com.ssg_order.ssg_order_api.claim.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, String> {
    int countByOrdNo(String ordNo);

    boolean existsByClaimNo(String claimNo);
}
