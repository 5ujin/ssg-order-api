package com.ssg_order.ssg_order_api.order.controller.dto;

import lombok.Data;

@Data
public class OrderCancelRequest {
    private final String ordNo;
    private final String prdNo;
}
