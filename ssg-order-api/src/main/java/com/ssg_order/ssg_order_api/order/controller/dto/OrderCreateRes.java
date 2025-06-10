package com.ssg_order.ssg_order_api.order.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateRes {
    @Schema(description = "주문번호", example = "20250610-461756")
    private final String ordNo;
    @Schema(description = "총판매가격", example = "15500")
    private final Long totalSalePrice;
    @Schema(description = "총결제금액", example = "13200")
    private final Long totalPayAmt;
    @Schema(description = "주문상세리스트", example = "[{\"prdNo\": \"1000000002\", \"payAmt\": 7400}, {\"prdNo\": \"1000000005\", \"payAmt\": 7800}]")
    private List<OrderCreateResItem> orderCreateResponseItemList;
}
