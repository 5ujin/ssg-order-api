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

    @Schema(description = "주문번호", example = "20250610-01428B")
    private final String ordNo;
    @Schema(description = "총결제금액", example = "34500")
    private final Long totalPayAmt;
    @Schema(description = "환불금액", example = "16000")
    private final Long refundAmt;
    @Schema(
            description = "주문상품리스트",
            example = """
                    [
                      {
                        "prdNo": "1000000002",
                        "prdNm": "신라면 멀티팩",
                        "ordQty": 5,
                        "payAmt": 16000,
                        "ordDtlStatus": "COMPLETED"
                      },
                      {
                        "prdNo": "1000000003",
                        "prdNm": "바나나 한 송이",
                        "ordQty": 2,
                        "payAmt": 6400,
                        "ordDtlStatus": "CANCELED"
                      }
                    ]"""
    )
    private final List<OrderProductResItem> orderProductResItemList;
}
