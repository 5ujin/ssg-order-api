package com.ssg_order.ssg_order_api.common.util;

import com.ssg_order.ssg_order_api.common.exception.BusinessException;
import com.ssg_order.ssg_order_api.common.exception.ErrorCode;
import com.ssg_order.ssg_order_api.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OrderNoGenerator {

    private final OrderRepository orderRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public String generateOrderNo() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int maxRetry = 5;

        for (int i = 0; i < maxRetry; i++) {
            String randomHex = generateRandomHex6();
            String orderNo = datePart + "-" + randomHex;

            if (!orderRepository.existsByOrdNo(orderNo)) {
                return orderNo;
            }
        }

        throw new BusinessException(ErrorCode.DUPLICATED_ORDER);
    }

    private String generateRandomHex6() {
        byte[] bytes = new byte[3]; // 3 bytes = 6 hex chars
        secureRandom.nextBytes(bytes);

        char[] hexChars = new char[6];
        for (int i = 0; i < 3; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
