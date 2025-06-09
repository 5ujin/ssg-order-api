package com.ssg_order.ssg_order_api.claim.service;

import com.ssg_order.ssg_order_api.claim.controller.dto.OrderCancelRequest;
import com.ssg_order.ssg_order_api.claim.controller.dto.OrderCancelResponse;

public interface ClaimService {

    public OrderCancelResponse orderCancel(OrderCancelRequest orderCancelRequest);
}
