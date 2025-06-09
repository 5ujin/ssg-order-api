package com.ssg_order.ssg_order_api.claim.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCancelResponse {

    private final String prdNo;
    private final String prdNm;
    private final Long salePrice;
    private final Long discountPrice;
    private final Long refundAmt;
    private final Long cancelableAmt;

}
