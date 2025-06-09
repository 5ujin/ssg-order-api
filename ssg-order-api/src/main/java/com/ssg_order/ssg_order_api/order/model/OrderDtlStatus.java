package com.ssg_order.ssg_order_api.order.model;

public enum OrderDtlStatus {
    ORDERED("주문 완료"),
    CANCELED("주문 취소"),
    PREPARING("상품 준비중"),
    SHIPPED("배송중"),
    DELIVERED("배송 완료"),
    RETURN_REQUESTED("반품 요청"),
    RETURNED("반품 완료"),
    EXCHANGED("교환 완료");

    private final String description;

    OrderDtlStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
