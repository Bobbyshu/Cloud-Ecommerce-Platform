package com.company.orderservice.dto;

public record EmailNotificationRequest(
        String to,
        String subject,
        String body
) {
}
