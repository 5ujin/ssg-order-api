package com.ssg_order.ssg_order_api.order.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderListRes {

    @Schema(description = "주문번호", example = "20250610-461756")
    private final String ordNo;
    @Schema(description = "총결제금액", example = "13200")
    private final Long totalPayAmt;
    @Schema(description = "환불금액", example = "3200")
    private final Long refundAmt;
    @Schema(
            description = "주문상세리스트",
            example = """
                    [
                      {
                        "prdNo": "1000000003",
                        "prdNm": "바나나 한 송이",
                        "ordQty": 1,
                        "payAmt": 3200
                      },
                      {
                        "prdNo": "1000000005",
                        "prdNm": "오리온 초코파이",
                        "ordQty": 3,
                        "payAmt": 7800
                      }
                    ]"""
    )
    private final List<OrderProductResItem> orderProductResItemList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderProductResItem {
        @Schema(description = "상품번호", example = "1000000003")
        private final String prdNo;
        @Schema(description = "상품명", example = "바나나 한 송이")
        private final String prdNm;
        @Schema(description = "주문수량", example = "1")
        private final int ordQty;
        @Schema(description = "결제금액", example = "3200")
        private final Long payAmt;
    }
}
