package com.ssg_order.ssg_order_api.order.service;

import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import com.ssg_order.ssg_order_api.common.util.OrderNoGenerator;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRes;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderListReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderListRes;
import com.ssg_order.ssg_order_api.order.entity.Order;
import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import com.ssg_order.ssg_order_api.order.entity.OrderItemHistory;
import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import com.ssg_order.ssg_order_api.order.model.OrderStatus;
import com.ssg_order.ssg_order_api.order.repository.OrderItemHistoryRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderItemRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderRepository;
import com.ssg_order.ssg_order_api.product.entity.Product;
import com.ssg_order.ssg_order_api.product.repository.ProductHistoryRepository;
import com.ssg_order.ssg_order_api.product.repository.ProductRepository;
import com.ssg_order.ssg_order_api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final ProductRepository productRepository;

    private final OrderNoGenerator orderNoGenerator;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemHistoryRepository orderItemHistoryRepository;

    private final ProductService productService;

    @Override
    @Transactional
    public OrderCreateRes createOrder(OrderCreateReq orderCreateReq) {
        try {
            List<OrderCreateReq.OrderCreateRequestItem> orderCreateRequestItemList = orderCreateReq.getOrderCreateRequestItemList();

            // 1. 유효성 검사(상품 존재 여부, 재고)
            validateProductsAndStock(orderCreateRequestItemList);

            // 2. 주문번호 채번
            String ordNo = orderNoGenerator.generateOrderNo();

            // 3. 주문저장
            List<OrderItem> orderItemList = saveOrder(ordNo, orderCreateRequestItemList);

            return buildOrderResponse(ordNo, orderItemList);
        } catch (BusinessException e) {
            log.warn("BusinessException occurred: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during cancelOrderProduct", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private void validateProductsAndStock(List<OrderCreateReq.OrderCreateRequestItem> itemList) {
        for (OrderCreateReq.OrderCreateRequestItem item : itemList) {
            Product product = productRepository.findByPrdNo(item.getPrdNo())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));

            if (product.getStock() < item.getOrdQty()) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            }
        }
    }

    private List<OrderItem> saveOrder(String ordNo, List<OrderCreateReq.OrderCreateRequestItem> orderCreateRequestItemList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        long totalPayAmt = 0L;

        // 주문 기본 저장
        Order order = Order.builder()
                .ordNo(ordNo)
                .userId("sujin3100")
                .ordStatus(OrderStatus.COMPLETED)
                .creator("sujin3100")
                .updater("sujin3100")
                .build();

        orderRepository.save(order);

        // 주문 상세 저장
        int index = 1; // 주문상세번호 채번용
        for (OrderCreateReq.OrderCreateRequestItem item: orderCreateRequestItemList) {
            Product product = productRepository.findByPrdNo(item.getPrdNo())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));

            // 상품 이력 저장
            productService.saveProductHistory(product);

            // 재고 차감
            product.decreaseStock(item.getOrdQty());

            long payAmt = (product.getSalePrice() - product.getDiscountPrice()) * (long) item.getOrdQty();
            totalPayAmt += payAmt;

            OrderItem orderItem = OrderItem.builder()
                    .ordSn(order.getOrdSn())
                    .ordNo(ordNo)
                    .ordDtlNo(index)
                    .ordDtlStatus(OrderDtlStatus.ORDERED)
                    .prdNo(item.getPrdNo())
                    .ordQty(item.getOrdQty())
                    .salePrice(product.getSalePrice())
                    .discountPrice(product.getDiscountPrice())
                    .payAmt(payAmt)
                    .creator("sujin3100")
                    .updater("sujin3100")
                    .build();

            orderItemRepository.save(orderItem);

            orderItemList.add(orderItem);

            index++;
        }

        // 주문금액 정보 update
        order.setTotalPayAmt(totalPayAmt);
        order.setCancelableAmt(totalPayAmt);
        orderRepository.save(order);


        return orderItemList;
    }

    private OrderCreateRes buildOrderResponse(String ordNo, List<OrderItem> orderItemList) {
        List<OrderCreateRes.OrderCreateResItem> orderCreateResponseItemList = orderItemList.stream()
                .map(item -> OrderCreateRes.OrderCreateResItem.builder()
                        .prdNo(item.getPrdNo())
                        .payAmt(item.getPayAmt())
                        .build())
                .toList();

        long totalSalePrice = orderItemList.stream()
                .mapToLong(item -> item.getSalePrice() * item.getOrdQty())
                .sum();
        long totalPayAmt = orderItemList.stream()
                .mapToLong(OrderItem::getPayAmt)
                .sum();

        return OrderCreateRes.builder()
                .ordNo(ordNo)
                .totalSalePrice(totalSalePrice)
                .totalPayAmt(totalPayAmt)
                .orderCreateResponseItemList(orderCreateResponseItemList)
                .build();
    }

    @Override
    @Transactional
    public void saveOrderDtlHistory(OrderItem orderItem) {
        OrderItemHistory history = OrderItemHistory.builder()
                .ordDtlSn(orderItem.getOrdDtlSn())
                .ordNo(orderItem.getOrdNo())
                .ordDtlNo(orderItem.getOrdDtlNo())
                .prdNo(Long.parseLong(orderItem.getPrdNo())) // 타입 변환 주의!
                .ordDtlStatus(orderItem.getOrdDtlStatus())
                .ordFinDtime(orderItem.getOrdFinDtime())
                .ordCnclDtime(orderItem.getOrdCnclDtime())
                .ordQty(orderItem.getOrdQty())
                .salePrice(orderItem.getSalePrice().intValue()) // Long → Integer 변환
                .discountPrice(orderItem.getDiscountPrice().intValue())
                .payAmt(orderItem.getPayAmt().intValue())
                .creator(orderItem.getCreator())
                .createdAt(orderItem.getCreatedAt())
                .updater(orderItem.getUpdater())
                .updatedAt(orderItem.getUpdatedAt())
                .build();

        orderItemHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListRes orderList(OrderListReq orderListReq) {
        try {
            String ordNo = orderListReq.getOrdNo();
            // 1. 주문번호 유효성 검사
            Order order = orderRepository.findByOrdNo(ordNo)
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_ORD_NO));

            // 2. 주문 리스트 조회
            List<OrderItem> orderItemList = order.getOrderItems();

            // 3. 응닶값 빌드
            List<OrderListRes.OrderProductResItem> productResItems = orderItemList.stream()
                    .map(item -> {
                        Product product = productRepository.findByPrdNo(item.getPrdNo())
                                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));
                        return OrderListRes.OrderProductResItem.builder()
                                .prdNo(product.getPrdNo())
                                .prdNm(product.getPrdName())
                                .ordQty(item.getOrdQty())
                                .payAmt(item.getPayAmt())
                                .build();
                    })
                    .toList();

            long refundAmt = orderItemList.stream()
                    .filter(item -> item.getOrdDtlStatus().isCanceled())
                    .mapToLong(OrderItem::getPayAmt)
                    .sum();

            return OrderListRes.builder()
                    .ordNo(order.getOrdNo())
                    .totalPayAmt(order.getTotalPayAmt())
                    .refundAmt(refundAmt)
                    .orderProductResItemList(productResItems)
                    .build();

        } catch (BusinessException e) {
            log.warn("BusinessException occurred: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during cancelOrderProduct", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }

    }
}
