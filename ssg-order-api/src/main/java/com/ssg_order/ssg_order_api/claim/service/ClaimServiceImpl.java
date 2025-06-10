package com.ssg_order.ssg_order_api.claim.service;

import com.ssg_order.ssg_order_api.claim.dto.ClaimValidationResult;
import com.ssg_order.ssg_order_api.claim.dto.OrderCancelReq;
import com.ssg_order.ssg_order_api.claim.dto.OrderCancelRes;
import com.ssg_order.ssg_order_api.claim.entity.Claim;
import com.ssg_order.ssg_order_api.claim.model.ClaimReason;
import com.ssg_order.ssg_order_api.claim.model.ClaimStatus;
import com.ssg_order.ssg_order_api.claim.model.ClaimType;
import com.ssg_order.ssg_order_api.claim.repository.ClaimRepository;
import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import com.ssg_order.ssg_order_api.common.util.ClaimNoGenerator;
import com.ssg_order.ssg_order_api.order.entity.Order;
import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import com.ssg_order.ssg_order_api.order.model.OrderStatus;
import com.ssg_order.ssg_order_api.order.repository.OrderItemRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderRepository;
import com.ssg_order.ssg_order_api.order.service.OrderService;
import com.ssg_order.ssg_order_api.product.entity.Product;
import com.ssg_order.ssg_order_api.product.repository.ProductRepository;
import com.ssg_order.ssg_order_api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final ClaimRepository claimRepository;

    private final ClaimNoGenerator claimNoGenerator;

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final OrderService orderService;

    @Override
    @Transactional
    public OrderCancelRes orderCancel(OrderCancelReq orderCancelReq) {
        try {
            String ordNo = orderCancelReq.getOrdNo();
            String prdNo = orderCancelReq.getPrdNo();

            // 1. 취소 가능 여부 확인
            ClaimValidationResult claimValidationResult = validateOrderAndProduct(ordNo, prdNo);

            // 2. 클레임번호 채번
            String claimNo = claimNoGenerator.generateClaimNo(ordNo);

            // 3. 클레임 저장
            saveClaim(claimNo, claimValidationResult);

            // 4. 주문상태 변경 및 주문금액 및 환불가능금액 update
            updateOrderStatusAndAmount(claimValidationResult);

            // 5. 재고 복원
            restoreProductStock(claimValidationResult.getOrderItem());

            // 6. 응답 생성
            return buildClaimResponse(claimValidationResult);

        } catch (BusinessException e) {
            log.warn("BusinessException occurred: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during cancelOrderProduct", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private ClaimValidationResult validateOrderAndProduct(String ordNo, String prdNo) {
        // 1. 주문 유효성 확인
        Order order = orderRepository.findByOrdNo(ordNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ORD_NO));

        // 2. 주문상세 (상품 단위) 유효성 확인
        OrderItem orderItem = orderItemRepository.findByOrdNoAndPrdNo(ordNo, prdNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ORDER_PRODUCT));

        // 3. 이미 취소된 상품인지 체크
        if (orderItem.getOrdDtlStatus() == OrderDtlStatus.CANCELED) {
            throw new BusinessException(ErrorCode.ALREADY_CANCELED_PRODUCT);
        }

        return new ClaimValidationResult(order, orderItem);
    }

    private void saveClaim(String claimNo, ClaimValidationResult claimValidationResult) {
        String ordNo = claimValidationResult.getOrderItem().getOrdNo();
        String prdNo = claimValidationResult.getOrderItem().getPrdNo();

        OrderItem orderItem = claimValidationResult.getOrderItem();

        Claim claim = Claim.builder()
                .claimNo(claimNo)
                .ordNo(ordNo)
                .prdNo(prdNo)
                .ordDtlNo(orderItem.getOrdDtlNo())
                .claimType(ClaimType.CANCEL) // 추후 고객 선택 값에 따라 변경
                .claimStatus(ClaimStatus.COMPLETED)
                .claimReason(ClaimReason.CUSTOMER_CHANGE)
                .claimQty(orderItem.getOrdQty())
                .claimDtime(LocalDateTime.now())
                .claimFinDtime(LocalDateTime.now())
                .refundAmt(orderItem.getPayAmt())
                .build();

        claimRepository.save(claim);
    }

    private void updateOrderStatusAndAmount(ClaimValidationResult claimValidationResult) {
        Order order = claimValidationResult.getOrder();
        OrderItem orderItem = claimValidationResult.getOrderItem();

        // 0. 주문상세이력 저장
        orderService.saveOrderDtlHistory(orderItem);

        // 1. 주문상세 상태 업데이트
        orderItem.setOrdDtlStatus(OrderDtlStatus.CANCELED);
        orderItem.setOrdCnclDtime(LocalDateTime.now());

        // 2. 주문 금액 차감 및 주문상태 업데이트
        long refundAmt = orderItem.getPayAmt();
        order.setCancelableAmt(order.getCancelableAmt() - refundAmt);

        // 3. 전체 주문상품이 모두 취소되었는지 확인
        List<OrderItem> allOrderItems = orderItemRepository.findAllByOrdNo(order.getOrdNo());

        boolean allCanceled = allOrderItems.stream()
                .allMatch(item -> item.getOrdDtlStatus() == OrderDtlStatus.CANCELED);

        if (allCanceled) {
            order.setOrdStatus(OrderStatus.CANCELED);
            order.setOrdCnclDtime(LocalDateTime.now());
        } else {
            order.setOrdStatus(OrderStatus.PARTIALLY_CANCELED);
        }
    }

    private void restoreProductStock(OrderItem orderItem) {
        Product product = productRepository.findByPrdNo(orderItem.getPrdNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));

        // 재고 복원 전 history 저장
        productService.saveProductHistory(product);

        product.increaseStock(orderItem.getOrdQty());
    }

    private OrderCancelRes buildClaimResponse(ClaimValidationResult claimValidationResult) {
        Order order = claimValidationResult.getOrder();
        OrderItem orderItem = claimValidationResult.getOrderItem();

        Product product = productRepository.findByPrdNo(orderItem.getPrdNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));

        return OrderCancelRes.builder()
                .prdNo(orderItem.getPrdNo())
                .prdNm(product.getPrdName())
                .salePrice(orderItem.getSalePrice())
                .discountPrice(orderItem.getDiscountPrice())
                .refundAmt(orderItem.getPayAmt())
                .cancelableAmt(order.getCancelableAmt())
                .build();
    }
}
