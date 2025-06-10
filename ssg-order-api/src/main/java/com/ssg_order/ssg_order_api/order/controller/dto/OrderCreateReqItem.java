package com.ssg_order.ssg_order_api.order.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderCreateReqItem {
    @NotBlank
    @Schema(description = "상품번호", example = "1000000003")
    private String prdNo;

    @Min(1)
    @Schema(description = "주문수량", example = "1")
    private int ordQty;
}
