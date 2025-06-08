package com.ssg_order.ssg_order_api.order.service;

import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRequest;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateResponse;

public interface OrderService {

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest);
}
