package com.ssg_order.ssg_order_api.order.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {

    @NotEmpty
    private List<@Valid OrderCreateRequestItem> orderCreateRequestItemList;

    @Data
    public static class OrderCreateRequestItem {
        @NotBlank
        private String productNo;

        @Min(1)
        private int ordQty;
    }
}
