package com.company.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailNotificationRequest(
		@Email(message = "Invalid recipient email") @NotBlank(message = "Recipient email is required") String to,
		@NotBlank(message = "Email subject is required") String subject,
		@NotBlank(message = "Email body is required") String body
) {
}
