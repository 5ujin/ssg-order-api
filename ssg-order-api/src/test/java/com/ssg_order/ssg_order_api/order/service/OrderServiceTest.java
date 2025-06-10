package com.ssg_order.ssg_order_api.order.service;

import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import com.ssg_order.ssg_order_api.common.util.OrderNoGenerator;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateReq;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateReqItem;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateRes;
import com.ssg_order.ssg_order_api.order.controller.dto.OrderCreateResItem;
import com.ssg_order.ssg_order_api.order.entity.Order;
import com.ssg_order.ssg_order_api.order.model.OrderStatus;
import com.ssg_order.ssg_order_api.order.repository.OrderItemHistoryRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderItemRepository;
import com.ssg_order.ssg_order_api.order.repository.OrderRepository;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ProductRepository productRepository;
    @Mock private OrderNoGenerator orderNoGenerator;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private OrderItemHistoryRepository orderItemHistoryRepository;
    @Mock private ProductService productService;

    @Test
    @DisplayName("주문 성공")
    void createOrder_success() {
        // -----------------------------------
        // given
        // -----------------------------------
        String prdNo1 = "1000000002";
        String prdNo2 = "1000000005";
        int ordQty1 = 2, ordQty2 = 3;
        long salePrice1 = 4200L, discountPrice1 = 500L;
        long salePrice2 = 3000L, discountPrice2 = 400L;
        long payAmt1 = (salePrice1 - discountPrice1) * ordQty1; // 7,400
        long payAmt2 = (salePrice2 - discountPrice2) * ordQty2; // 7,800
        long totalSalePrice = salePrice1 * ordQty1 + salePrice2 * ordQty2; // 17,400
        long totalPayAmt = payAmt1 + payAmt2; // 15,200

        // 요청 DTO
        OrderCreateReqItem item1 = new OrderCreateReqItem();
        item1.setPrdNo(prdNo1);
        item1.setOrdQty(ordQty1);
        OrderCreateReqItem item2 = new OrderCreateReqItem();
        item2.setPrdNo(prdNo2);
        item2.setOrdQty(ordQty2);
        OrderCreateReq req = new OrderCreateReq();
        req.setOrderCreateRequestItemList(List.of(item1, item2));

        // 상품 Mock
        Product product1 = Product.builder()
                .prdNo(prdNo1)
                .prdName("신라면 멀티팩")
                .stock(500)
                .salePrice(salePrice1)
                .discountPrice(discountPrice1)
                .build();

        Product product2 = Product.builder()
                .prdNo(prdNo2)
                .prdName("오리온 초코파이")
                .stock(300)
                .salePrice(salePrice2)
                .discountPrice(discountPrice2)
                .build();

        // 주문 Mock
        Order mockOrder = Order.builder()
                .ordNo("TEST_ORD12345")
                .ordSn(1)
                .userId("sujin3100")
                .ordStatus(OrderStatus.COMPLETED)
                .build();

        given(productRepository.findByPrdNo(prdNo1)).willReturn(Optional.of(product1));
        given(productRepository.findByPrdNo(prdNo2)).willReturn(Optional.of(product2));
        given(orderNoGenerator.generateOrderNo()).willReturn("TEST_ORD12345");
        given(orderRepository.save(any(Order.class))).willReturn(mockOrder);

        // -----------------------------------
        // WHEN
        // -----------------------------------
        OrderCreateRes res = orderService.createOrder(req);

        // -----------------------------------
        // THEN
        // 필수 필드
        assertThat(res).isNotNull();
        assertThat(res.getOrdNo()).isNotBlank();

        // 금액 확인
        assertThat(res.getTotalPayAmt()).isEqualTo(totalPayAmt);
        assertThat(res.getTotalSalePrice()).isEqualTo(totalSalePrice);

        // 항목별 응답 검증
        List<OrderCreateResItem> items = res.getOrderCreateResponseItemList();
        assertThat(items).hasSize(2);

        Map<String, Long> payAmtMap = items.stream()
                .collect(Collectors.toMap(OrderCreateResItem::getPrdNo, OrderCreateResItem::getPayAmt));

        assertThat(payAmtMap.get(prdNo1)).isEqualTo(payAmt1);
        assertThat(payAmtMap.get(prdNo2)).isEqualTo(payAmt2);

    }

    @Test
    @DisplayName("주문 실패 - 존재하지 않는 상품번호")
    void createOrder_fail_invalidProductNo() {
        // -----------------------------------
        // GIVEN
        // -----------------------------------
        String invalidPrdNo = "INVALID_PRD001";
        OrderCreateReqItem invalidItem = new OrderCreateReqItem();
        invalidItem.setPrdNo(invalidPrdNo);
        invalidItem.setOrdQty(2);

        OrderCreateReq req = new OrderCreateReq();
        req.setOrderCreateRequestItemList(List.of(invalidItem));

        // 상품 미존재 상황
        given(productRepository.findByPrdNo(invalidPrdNo)).willReturn(Optional.empty());

        // -----------------------------------
        // WHEN / THEN
        // -----------------------------------
        assertThatThrownBy(() -> orderService.createOrder(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INVALID_PRODUCT_NO.getMessage());

        try {
            orderService.createOrder(req);
            fail("예외가 발생해야 합니다");
        } catch (BusinessException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.INVALID_PRODUCT_NO);
        }
    }

    @Test
    @DisplayName("주문 실패 - 재고 부족")
    void createOrder_fail_outOfStock() {
        // -----------------------------------
        // GIVEN
        // -----------------------------------
        String prdNo = "1000000003";
        int orderQty = 300; // 재고보다 많은 수량
        long salePrice = 3500L;
        long discountPrice = 300L;

        // 요청 DTO
        OrderCreateReqItem item = new OrderCreateReqItem();
        item.setPrdNo(prdNo);
        item.setOrdQty(orderQty);

        OrderCreateReq req = new OrderCreateReq();
        req.setOrderCreateRequestItemList(List.of(item));

        // 상품: 재고는 200개뿐
        Product product = Product.builder()
                .prdNo(prdNo)
                .prdName("바나나 한 송이")
                .stock(200) // 실제 재고 부족
                .salePrice(salePrice)
                .discountPrice(discountPrice)
                .build();

        given(productRepository.findByPrdNo(prdNo)).willReturn(Optional.of(product));

        // -----------------------------------
        // WHEN / THEN
        // -----------------------------------
        assertThatThrownBy(() -> orderService.createOrder(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.OUT_OF_STOCK.getMessage());

        try {
            orderService.createOrder(req);
            fail("예외가 발생해야 합니다.");
        } catch (BusinessException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.OUT_OF_STOCK);
        }
    }

}