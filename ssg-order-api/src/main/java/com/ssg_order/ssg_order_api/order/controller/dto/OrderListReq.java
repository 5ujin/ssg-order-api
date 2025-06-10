package com.ssg_order.ssg_order_api.order.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderListReq {
    @NotBlank
    private String ordNo;
}
