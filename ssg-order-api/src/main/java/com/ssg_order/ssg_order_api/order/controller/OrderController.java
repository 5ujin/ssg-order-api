package com.ssg_order.ssg_order_api.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRequest;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    @PostMapping("/createOrder")
    @ResponseBody
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest) {

        // TODO: 로그인 여부 확인(세션 또는 토큰 기반)


        log.info("주문 생성 요청, {}", orderCreateRequest);
        OrderCreateResponse orderCreateResponse = OrderCreateResponse.builder()
                .ordNo("2025060812345")
                .totalAmt(10000L)
                .orderCreateResponseItemList(List.of())
                .build();
        return ResponseEntity.ok(orderCreateResponse);
    }

}
