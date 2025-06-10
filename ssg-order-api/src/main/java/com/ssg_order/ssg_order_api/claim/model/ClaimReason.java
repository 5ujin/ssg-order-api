package com.ssg_order.ssg_order_api.claim.model;

public enum ClaimReason {
    CUSTOMER_CHANGE("단순 변심"),
    PRODUCT_DEFECT("상품 불량"),
    WRONG_DELIVERY("오배송"),
    LATE_DELIVERY("배송 지연"),
    OTHER("기타");

    private final String description;

    ClaimReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
