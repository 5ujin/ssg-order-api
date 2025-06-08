package com.ssg_order.ssg_order_api.order.service;

import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRequest;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    @Override
    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) {
        return null;
    }

    private boolean validateProductsAndStock() {
        return true;
    }

    private String generateOrderNo() {
        return "2025060912345";
    }

    private int saveOrder() {
        return 1;
    }

    private int updateStock() {
        return 1;
    }

    private OrderCreateResponse buildOrderResponse() {
        return OrderCreateResponse.builder()
                .ordNo("2025060812345")
                .totalAmt(10000L)
                .orderCreateResponseItemList(List.of())
                .build();
    }
}
