package com.ssg_order.ssg_order_api.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderListRes {

    private final String ordNo;
    private final Long totalPayAmt;
    private final Long refundAmt;
    private final List<OrderProductResItem> orderProductResItemList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderProductResItem {
        private final String prdNo;
        private final String prdNm;
        private final int ordQty;
        private final Long payAmt;
    }
}
