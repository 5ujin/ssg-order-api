package com.ssg_order.ssg_order_api.order.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateReq {

    @NotEmpty
    @Schema(description = "주문요청리스트", example = "[{\"prdNo\": \"1000000002\", \"ordQty\": 1}]")
    private List<@Valid OrderCreateReqItem> orderCreateRequestItemList;
}
