package com.ssg_order.ssg_order_api.order.service;

import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRes;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderListReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderListRes;
import com.ssg_order.ssg_order_api.order.entity.OrderItem;

public interface OrderService {

    public OrderCreateRes createOrder(OrderCreateReq orderCreateReq);

    public OrderListRes orderList(OrderListReq orderListReq);

    public void saveOrderDtlHistory(OrderItem orderItem);
}
