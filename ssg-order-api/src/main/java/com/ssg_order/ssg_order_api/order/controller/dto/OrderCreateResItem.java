package com.ssg_order.ssg_order_api.order.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResItem {
    @Schema(description = "상품번호", example = "1000000003")
    private final String prdNo;
    @Schema(description = "결제금액(실구매가)", example = "3200")
    private final Long payAmt;
}
