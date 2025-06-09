package com.ssg_order.ssg_order_api.claim.service;

import com.ssg_order.ssg_order_api.claim.controller.dto.OrderCancelRequest;
import com.ssg_order.ssg_order_api.claim.controller.dto.OrderCancelResponse;
import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import com.ssg_order.ssg_order_api.order.entity.Order;
import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import com.ssg_order.ssg_order_api.order.repository.OrderItemRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ClaimServiceImpl implements ClaimService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ClaimServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public OrderCancelResponse orderCancel(OrderCancelRequest orderCancelRequest) {
        try {
            Order order = validateOrderAndProduct(orderCancelRequest.getOrdNo(), orderCancelRequest.getPrdNo());
            System.out.println("order = " + order);
        } catch (BusinessException e) {
            log.warn("BusinessException occurred: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during cancelOrderProduct", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
        return null;
    }

    private Order validateOrderAndProduct(String ordNo, String prdNo) {
        // 1. 주문 유효성 확인
        Order order = orderRepository.findByOrdNo(ordNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ORD_NO));

        // 2. 주문상세 (상품 단위) 유효성 확인
        OrderItem item = orderItemRepository.findByOrdNoAndPrdNo(ordNo, prdNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ORDER_PRODUCT));

        // 3. 이미 취소된 상품인지 체크
        if (item.getOrdDtlStatus() == OrderDtlStatus.CANCELED) {
            throw new BusinessException(ErrorCode.ALREADY_CANCELED_PRODUCT);
        }

        return order;
    }
}
