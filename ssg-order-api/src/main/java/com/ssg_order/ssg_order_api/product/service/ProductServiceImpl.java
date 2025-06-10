package com.ssg_order.ssg_order_api.product.service;

import com.ssg_order.ssg_order_api.product.entity.Product;
import com.ssg_order.ssg_order_api.product.entity.ProductHistory;
import com.ssg_order.ssg_order_api.product.repository.ProductHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductHistoryRepository productHistoryRepository;

    public ProductServiceImpl(ProductHistoryRepository productHistoryRepository) {
        this.productHistoryRepository = productHistoryRepository;
    }

    @Override
    public void saveProductHistory(Product product) {
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
