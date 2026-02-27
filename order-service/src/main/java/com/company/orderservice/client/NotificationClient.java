package com.company.orderservice.client;

import com.company.orderservice.dto.EmailNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/notifications/email")
    void sendEmail(@RequestBody EmailNotificationRequest request);
}
