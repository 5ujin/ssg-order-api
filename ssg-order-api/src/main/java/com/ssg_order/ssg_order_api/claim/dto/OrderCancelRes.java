package com.ssg_order.ssg_order_api.claim.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCancelRes {

    @Schema(description = "상품번호", example = "1000000003")
    private final String prdNo;
    @Schema(description = "상품명", example = "바나나 한 송이")
    private final String prdNm;
    @Schema(description = "판매가격", example = "3500")
    private final Long salePrice;
    @Schema(description = "할인금액", example = "300")
    private final Long discountPrice;
    @Schema(description = "환불금액", example = "3200")
    private final Long refundAmt;
    @Schema(description = "취소가능금액", example = "2600")
    private final Long cancelableAmt;

}
