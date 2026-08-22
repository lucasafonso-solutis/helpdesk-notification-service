package solutis.lucas.afonso.helpdesk.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.net.URI;

import solutis.lucas.afonso.helpdesk.dtos.NotificationDTO;
import solutis.lucas.afonso.helpdesk.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Operation(summary = "Create Notification", description = "Create Notification")
    @ApiResponse(responseCode = "201", description = "Create Notification")
	@PostMapping
	public ResponseEntity<NotificationDTO> create(@RequestBody NotificationDTO notificationDTO, UriComponentsBuilder uriComponentsBuilder) {
		NotificationDTO notification = this.notificationService.create(notificationDTO);
		URI uri = uriComponentsBuilder.path("/notifications/{id}").buildAndExpand(notification.id()).toUri();

		return ResponseEntity.created(uri).body(notification);
	}

	@Operation(summary = "List All Notifications", description = "List All Notifications")
    @ApiResponse(responseCode = "200", description = "List All Notifications")
	@GetMapping
	public List<NotificationDTO> findAll() {
		return notificationService.findAll();
	}

	@Operation(summary = "List Notification by ID", description = "List Notification By ID")
    @ApiResponse(responseCode = "200", description = "List Notification By ID")
	@GetMapping("/{id}")
	public ResponseEntity<NotificationDTO> findById(@PathVariable Long id) {
		return notificationService.findById(id)
				.stream()
				.findFirst()
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}