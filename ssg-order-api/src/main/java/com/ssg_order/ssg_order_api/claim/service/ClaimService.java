package com.ssg_order.ssg_order_api.claim.service;

import com.ssg_order.ssg_order_api.claim.dto.OrderCancelReq;
import com.ssg_order.ssg_order_api.claim.dto.OrderCancelRes;

public interface ClaimService {

    public OrderCancelRes orderCancel(OrderCancelReq orderCancelReq);
}
