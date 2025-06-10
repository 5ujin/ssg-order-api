package com.ssg_order.ssg_order_api.order.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderListReq {
    @NotBlank
    @Schema(description = "주문번호", example = "20250610-01428B")
    private String ordNo;
}
