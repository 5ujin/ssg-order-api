package com.ssg_order.ssg_order_api.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResponse {
    private final String ordNo;
    private final Long totalAmt;
    private List<OrderCreateResponseItem> orderCreateResponseItemList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderCreateResponseItem {
        private final String productNo;
        private final Long payAmt;
    }
}
