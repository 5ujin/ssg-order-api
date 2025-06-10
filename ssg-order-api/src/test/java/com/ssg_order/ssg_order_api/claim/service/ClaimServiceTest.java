package com.ssg_order.ssg_order_api.claim.service;

import com.ssg_order.ssg_order_api.claim.dto.OrderCancelReq;
import com.ssg_order.ssg_order_api.claim.dto.OrderCancelRes;
import com.ssg_order.ssg_order_api.claim.repository.ClaimRepository;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @InjectMocks
    private ClaimServiceImpl claimService;

    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ClaimRepository claimRepository;
    @Mock private ClaimNoGenerator claimNoGenerator;
    @Mock private ProductRepository productRepository;
    @Mock private ProductService productService;
    @Mock private OrderService orderService;

    @Test
    @DisplayName("정상주문취소 테스트")
    void orderCancel_success() {
        // -----------------------------------
        // GIVEN
        // -----------------------------------
        String ordNo = "TEST_ORD12345";
        String prdNo = "1000000002";
        long salePrice = 4200L;
        long discountPrice = 500L;
        int ordQty = 2;
        long payAmt = (salePrice - discountPrice) * ordQty; // 7400

        Order order = Order.builder()
                .ordNo(ordNo)
                .ordSn(1)
                .ordStatus(OrderStatus.COMPLETED)
                .cancelableAmt(payAmt)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .ordNo(ordNo)
                .prdNo(prdNo)
                .ordQty(ordQty)
                .salePrice(salePrice)
                .discountPrice(discountPrice)
                .payAmt(payAmt)
                .ordDtlStatus(OrderDtlStatus.ORDERED)
                .build();

        Product product = Product.builder()
                .prdNo(prdNo)
                .prdName("신라면 멀티팩")
                .stock(100)
                .salePrice(salePrice)
                .discountPrice(discountPrice)
                .build();

        given(orderRepository.findByOrdNo(ordNo)).willReturn(Optional.of(order));
        given(orderItemRepository.findByOrdNoAndPrdNo(ordNo, prdNo)).willReturn(Optional.of(orderItem));
        given(claimNoGenerator.generateClaimNo(ordNo)).willReturn("CLM0001");
        given(productRepository.findByPrdNo(prdNo)).willReturn(Optional.of(product));
        given(orderItemRepository.findAllByOrdNo(ordNo)).willReturn(List.of(orderItem));

        OrderCancelReq req = new OrderCancelReq(ordNo, prdNo);

        // -----------------------------------
        // WHEN
        // -----------------------------------
        OrderCancelRes res = claimService.orderCancel(req);

        // -----------------------------------
        // THEN
        // 응답 필드 검증
        assertThat(res).isNotNull();
        assertThat(res.getPrdNo()).isEqualTo(prdNo);
        assertThat(res.getPrdNm()).isEqualTo("신라면 멀티팩");
        assertThat(res.getSalePrice()).isEqualTo(salePrice);
        assertThat(res.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(res.getRefundAmt()).isEqualTo(payAmt);
        assertThat(res.getCancelableAmt()).isEqualTo(0L); // 전액 취소 가정

        // 주문 상태가 "CANCELED"로 변경되었는지 확인
        assertThat(order.getOrdStatus()).isEqualTo(OrderStatus.CANCELED);

        // 히스토리 저장 호출 여부 확인
        verify(orderService, times(1)).saveOrderDtlHistory(orderItem);

        // 상품 이력 저장 + 재고 복원 확인
        verify(productService, times(1)).saveProductHistory(product);
        assertThat(product.getStock()).isEqualTo(102); // 복원됨
    }
}