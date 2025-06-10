package com.ssg_order.ssg_order_api.common.util;

import com.ssg_order.ssg_order_api.claim.repository.ClaimRepository;
import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClaimNoGenerator
{
    private final ClaimRepository claimRepository;

    public String generateClaimNo(String ordNo) {
        int maxRetry = 5;

        for (int i = 0; i < maxRetry; i++) {
            int existingCount = claimRepository.countByOrdNo(ordNo);
            String claimNo = formatClaimNo(ordNo, existingCount + i + 1);

            if (!claimRepository.existsByClaimNo(claimNo)) {
                return claimNo;
            }
        }

        throw new BusinessException(ErrorCode.DUPLICATED_CLAIM);
    }

    private String formatClaimNo(String ordNo, int seq) {
        return String.format("%s-%03d", ordNo, seq);
    }
}
