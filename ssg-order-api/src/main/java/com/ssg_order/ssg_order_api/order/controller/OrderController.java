package com.ssg_order.ssg_order_api.order.controller;

import com.ssg_order.ssg_order_api.order.controller.dto.OrderListReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderListRes;
import com.ssg_order.ssg_order_api.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "주문 API", description = "주문생성 및 조회")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/createOrder")
    @ResponseBody
    @Operation(summary = "주문 생성", description = "상품번호와 주문수량으로 주문을 생성합니다.")
    public ResponseEntity<OrderCreateRes> createOrder(@RequestBody @Valid OrderCreateReq orderCreateReq) {

        // TODO: 로그인 여부 확인(세션 또는 토큰 기반)


        log.info("주문 생성 요청, {}", orderCreateReq);
        OrderCreateRes orderCreateRes = orderService.createOrder(orderCreateReq);
        return ResponseEntity.ok(orderCreateRes);
    }

    @PostMapping("/orderList")
    @ResponseBody
    @Operation(summary = "주문 조회", description = "주문번호로 주문리스트를 조회합니다.")
    public ResponseEntity<OrderListRes> getOrderList(@RequestBody @Valid OrderListReq orderListReq) {
        // TODO: 로그인 여부 확인(세션 또는 토큰 기반)

        log.info("주문 조회 요청, {}", orderListReq);
        OrderListRes orderListRes = orderService.orderList(orderListReq);
        return ResponseEntity.ok(orderListRes);
    }

}
