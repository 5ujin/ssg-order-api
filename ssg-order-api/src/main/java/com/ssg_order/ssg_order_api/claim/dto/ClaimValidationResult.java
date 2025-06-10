package com.ssg_order.ssg_order_api.claim.dto;

import com.ssg_order.ssg_order_api.order.entity.Order;
import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClaimValidationResult {

    private final Order order;
    private final OrderItem orderItem;
}
