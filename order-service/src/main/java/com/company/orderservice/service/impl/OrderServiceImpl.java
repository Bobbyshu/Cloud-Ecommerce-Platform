package com.company.orderservice.service.impl;

import com.company.orderservice.client.ProductClient;
import com.company.orderservice.dto.ProductDto;
import com.company.orderservice.entity.Order;
import com.company.orderservice.enums.OrderStatus;
import com.company.orderservice.dao.OrderRepository;
import com.company.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Override
    public Order createOrder(Order order) {
        // 1. remote get price
        ProductDto product = productClient.getProductById(order.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found: " + order.getProductId());
        }

        // 2. cal total price
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        order.setTotalPrice(totalPrice);

        // 3. set status and time
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }
}