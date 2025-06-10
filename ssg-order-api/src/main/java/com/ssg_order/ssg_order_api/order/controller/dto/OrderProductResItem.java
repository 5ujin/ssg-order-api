package com.ssg_order.ssg_order_api.order.controller.dto;

import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductResItem {
    @Schema(description = "상품번호", example = "1000000002")
    private final String prdNo;
    @Schema(description = "상품명", example = "신라면 멀티팩")
    private final String prdNm;
    @Schema(description = "주문수량", example = "5")
    private final int ordQty;
    @Schema(description = "결제금액", example = "16000")
    private final Long payAmt;
    @Schema(description = "주문상세상태", example = "COMPLETED")
    private final OrderDtlStatus ordDtlStatus;
}
