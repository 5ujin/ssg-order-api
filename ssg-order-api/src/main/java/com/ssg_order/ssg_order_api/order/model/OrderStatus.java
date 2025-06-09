package com.ssg_order.ssg_order_api.order.model;

public enum OrderStatus {
    CREATED("주문 생성됨"),
    COMPLETED("결제 완료"),
    CANCELED("전체 주문 취소"),
    PARTIALLY_CANCELED("일부 상품 취소"),
    SHIPPED("배송 시작"),
    DELIVERED("배송 완료"),
    RETURNED("전체 반품"),
    FAILED("결제 실패");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
