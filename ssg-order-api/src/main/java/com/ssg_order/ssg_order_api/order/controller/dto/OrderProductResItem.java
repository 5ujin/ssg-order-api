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
    @Schema(description = "상품번호", example = "1000000003")
    private final String prdNo;
    @Schema(description = "상품명", example = "바나나 한 송이")
    private final String prdNm;
    @Schema(description = "주문수량", example = "1")
    private final int ordQty;
    @Schema(description = "결제금액", example = "3200")
    private final Long payAmt;
    @Schema(description = "주문상세상태", example = "COMPLETED")
    private final OrderDtlStatus ordDtlStatus;
}
