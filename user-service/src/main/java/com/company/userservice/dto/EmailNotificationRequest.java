package com.company.userservice.dto;

public record EmailNotificationRequest(
        String to,
        String subject,
        String body
) {
}
