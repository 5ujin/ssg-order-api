package com.ssg_order.ssg_order_api.claim.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderCancelRequest {
    @NotBlank
    private final String ordNo;

    @NotBlank
    private final String prdNo;
}
