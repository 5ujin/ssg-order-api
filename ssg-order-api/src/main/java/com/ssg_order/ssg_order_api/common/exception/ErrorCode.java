package com.ssg_order.ssg_order_api.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNAUTHORIZED("GEN_401", 401, "로그인이 필요합니다."),
    INVALID_PARAM("ORD_400_01", 400, "필수 파라미터가 누락되었습니다."),
    INVALID_QTY("ORD_400_02", 400,"주문 수량은 1 이상이어야합니다."),
    OUT_OF_STOCK("ORD_400_03", 400,"상품 재고가 부족합니다."),
    INVALID_PRODUCT_NO("ORD_404_01", 404, "유효하지 않은 상품번호입니다."),
    DUPLICATED_ORDER("ORD_409_01", 409,  "이미 처리된 주문입니다."),
    INTERNAL_ERROR("GEN_500_01", 500, "알 수 없는 서버 오류입니다.");

    private final String code;
    private final int status;
    private final String message;

}
