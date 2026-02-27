package com.company.notificationservice.controller;

import com.company.notificationservice.dto.EmailNotificationRequest;
import com.company.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@PostMapping("/email")
	public ResponseEntity<Map<String, String>> sendEmail(@Valid @RequestBody EmailNotificationRequest request) {
		notificationService.sendEmail(request.to(), request.subject(), request.body());
		return ResponseEntity.ok(Map.of("message", "Email notification sent successfully"));
	}
}
