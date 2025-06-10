package com.ssg_order.ssg_order_api.claim.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderCancelReq {
    @NotBlank
    @Schema(description = "주문번호", example = "20250610-461756")
    private final String ordNo;

    @NotBlank
    @Schema(description = "상품번호", example = "1000000003")
    private final String prdNo;
}
