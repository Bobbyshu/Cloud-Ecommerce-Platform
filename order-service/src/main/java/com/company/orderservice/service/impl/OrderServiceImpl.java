package com.company.orderservice.service.impl;

import com.company.orderservice.client.NotificationClient;
import com.company.orderservice.client.ProductClient;
import com.company.orderservice.client.UserClient;
import com.company.orderservice.dto.EmailNotificationRequest;
import com.company.orderservice.dao.OrderRepository;
import com.company.orderservice.dto.ProductDto;
import com.company.orderservice.dto.UserDto;
import com.company.orderservice.entity.Order;
import com.company.orderservice.enums.OrderStatus;
import com.company.orderservice.exception.InsufficientStockException;
import com.company.orderservice.exception.ProductNotFoundException;
import com.company.orderservice.exception.UserIdInvalidException;
import com.company.orderservice.exception.UserNotFoundException;
import com.company.orderservice.service.OrderService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public Order createOrder(Order order, String loggedInUserId) {
        if (!loggedInUserId.equals(order.getUserId().toString())) {
            throw new UserIdInvalidException("Access Denied: You cannot create orders for others!");
        }
        // 1. check user exist
        UserDto user;
        try {
            user = userClient.getUserById(order.getUserId());
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException(order.getUserId());
        }

        // 2. check product exist
        ProductDto product;
        try {
            product = productClient.getProductById(order.getProductId());
        } catch (FeignException.NotFound e) {
            throw new ProductNotFoundException(order.getProductId());
        }

        if (product == null) {
            throw new ProductNotFoundException(order.getProductId());
        }

        // 3. check stock
        if (product.getStock() < order.getQuantity()) {
            throw new InsufficientStockException("Out of stock for product ID: " + order.getProductId() +
                    ", there's only: " + product.getStock() + " remain");
        }

        // 4. decrease stock
        try {
            productClient.reduceStock(order.getProductId(), order.getQuantity());
        } catch (FeignException.BadRequest e) {
            throw new InsufficientStockException("Reduce stock unsuccessfully: no enough stock");
        } catch (Exception e) {
            throw new RuntimeException("Unknown error happen: " + e.getMessage());
        }

        // cal total price
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        sendOrderNotification(user, product, savedOrder);

        return savedOrder;
    }

    private void sendOrderNotification(UserDto user, ProductDto product, Order savedOrder) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        String subject = "Order created successfully - #" + savedOrder.getId();
        String body = "Hi " + user.getUsername() + ", your order has been created."
                + "\nOrder ID: " + savedOrder.getId()
                + "\nProduct: " + product.getName()
                + "\nQuantity: " + savedOrder.getQuantity()
                + "\nTotal: " + savedOrder.getTotalPrice();

        try {
            notificationClient.sendEmail(new EmailNotificationRequest(user.getEmail(), subject, body));
        } catch (Exception exception) {
            log.warn("Order {} created, but notification send failed: {}", savedOrder.getId(), exception.getMessage());
        }
    }
}
