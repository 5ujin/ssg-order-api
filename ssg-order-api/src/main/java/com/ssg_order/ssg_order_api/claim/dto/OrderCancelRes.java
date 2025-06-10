package com.ssg_order.ssg_order_api.claim.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCancelRes {

    @Schema(description = "상품번호", example = "1000000002")
    private final String prdNo;
    @Schema(description = "상품명", example = "신라면 멀티팩")
    private final String prdNm;
    @Schema(description = "판매가격", example = "4200")
    private final Long salePrice;
    @Schema(description = "할인금액", example = "500")
    private final Long discountPrice;
    @Schema(description = "환불금액", example = "8400")
    private final Long refundAmt;
    @Schema(description = "남은환불가능금액", example = "20000")
    private final Long cancelableAmt;

}
