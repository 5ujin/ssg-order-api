package com.ssg_order.ssg_order_api.order.service;

import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import com.ssg_order.ssg_order_api.common.util.OrderNoGenerator;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRequest;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateResponse;
import com.ssg_order.ssg_order_api.order.entity.Order;
import com.ssg_order.ssg_order_api.order.entity.OrderItem;
import com.ssg_order.ssg_order_api.order.model.OrderDtlStatus;
import com.ssg_order.ssg_order_api.order.model.OrderStatus;
import com.ssg_order.ssg_order_api.order.repository.OrderItemRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderRepository;
import com.ssg_order.ssg_order_api.product.entity.Product;
import com.ssg_order.ssg_order_api.product.entity.ProductHistory;
import com.ssg_order.ssg_order_api.product.repository.ProductHistoryRepository;
import com.ssg_order.ssg_order_api.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    private final ProductRepository productRepository;

    private final OrderNoGenerator orderNoGenerator;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductHistoryRepository productHistoryRepository;

    public OrderServiceImpl(ProductRepository productRepository, OrderNoGenerator orderNoGenerator, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductHistoryRepository productHistoryRepository) {
        this.productRepository = productRepository;
        this.orderNoGenerator = orderNoGenerator;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productHistoryRepository = productHistoryRepository;
    }

    @Override
    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) {
        OrderCreateResponse orderCreateResponse = null;
        try {
            List<OrderCreateRequest.OrderCreateRequestItem> orderCreateRequestItemList = orderCreateRequest.getOrderCreateRequestItemList();

            // 1. 유효성 검사(상품 존재 여부, 재고)
            validateProductsAndStock(orderCreateRequestItemList);

            // 2. 주문번호 채번
            String ordNo = orderNoGenerator.generateOrderNo();

            // 3. 주문저장
            List<OrderItem> orderItemList = saveOrder(ordNo, orderCreateRequestItemList);

            orderCreateResponse = buildOrderResponse(ordNo, orderItemList);
        } catch (BusinessException e) {
            log.warn("비즈니스 예외 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("주문 생성 중 알 수 없는 서버 오류", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }

        return orderCreateResponse;
    }

    private void validateProductsAndStock(List<OrderCreateRequest.OrderCreateRequestItem> itemList) {
        for (OrderCreateRequest.OrderCreateRequestItem item : itemList) {
            Product product = productRepository.findByPrdNo(item.getPrdNo())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));

            if (product.getStock() < item.getOrdQty()) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            }
        }
    }

    private List<OrderItem> saveOrder(String ordNo, List<OrderCreateRequest.OrderCreateRequestItem> orderCreateRequestItemList) {
        // 주문 기본 저장
        Order order = Order.builder()
                .ordNo(ordNo)
                .userId("sujin3100")
                .ordStatus(OrderStatus.CREATED)
                .creator("sujin3100")
                .updater("sujin3100")
                .build();

        orderRepository.save(order);

        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문 상세 저장
        int index = 1; // 주문상세번호 채번용
        for (OrderCreateRequest.OrderCreateRequestItem item: orderCreateRequestItemList) {
            Product product = productRepository.findByPrdNo(item.getPrdNo())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PRODUCT_NO));

            // 상품 이력 저장
            saveProductHistory(product);

            // 재고 차감
            product.decreaseStock(item.getOrdQty());
            productRepository.save(product);

            Long payAmt = (product.getSalePrice() - product.getDiscountPrice()) * (long) item.getOrdQty();

            OrderItem orderItem = OrderItem.builder()
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

        return orderItemList;
    }

    private OrderCreateResponse buildOrderResponse(String ordNo, List<OrderItem> orderItemList) {
        List<OrderCreateResponse.OrderCreateResponseItem> orderCreateResponseItemList = orderItemList.stream()
                .map(item -> OrderCreateResponse.OrderCreateResponseItem.builder()
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

        return OrderCreateResponse.builder()
                .ordNo(ordNo)
                .totalSalePrice(totalSalePrice)
                .totalPayAmt(totalPayAmt)
                .orderCreateResponseItemList(orderCreateResponseItemList)
                .build();
    }

    private void saveProductHistory(Product product) {
        ProductHistory productHistory = ProductHistory.builder()
                .prdSn(product.getPrdSn())
                .prdNo(product.getPrdNo())
                .prdName(product.getPrdName())
                .salePrice(product.getSalePrice())
                .discountPrice(product.getDiscountPrice())
                .stock(product.getStock()) // 차감 전 재고
                .creator(product.getCreator())
                .createdAt(product.getCreatedAt())
                .updater(product.getUpdater())
                .updatedAt(product.getUpdatedAt())
                .histCreator("sujin3100")
                .build();

        productHistoryRepository.save(productHistory);
    }
}
