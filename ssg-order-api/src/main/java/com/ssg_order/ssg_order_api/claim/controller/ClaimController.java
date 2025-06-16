package com.ssg_order.ssg_order_api.claim.controller;

import com.ssg_order.ssg_order_api.claim.dto.OrderCancelReq;
import com.ssg_order.ssg_order_api.claim.dto.OrderCancelRes;
import com.ssg_order.ssg_order_api.claim.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/claim")
@RequiredArgsConstructor
@Tag(name = "클레임 API", description = "클레임 관련")
public class ClaimController {

    private final ClaimService claimService;

    @PatchMapping("/cancel")
    @Operation(summary = "클레임 생성", description = "주문번호와 상품번호로 클레임을 생성합니다.")
    public ResponseEntity<OrderCancelRes> cancelOrder(@RequestBody @Valid OrderCancelReq orderCancelReq) {
        // TODO: 로그인 여부 확인(세션 또는 토큰 기반)

        log.info("주문 취소 요청, {}", orderCancelReq);
        OrderCancelRes orderCancelRes = claimService.orderCancel(orderCancelReq);
        return ResponseEntity.ok(orderCancelRes);
    }
}
