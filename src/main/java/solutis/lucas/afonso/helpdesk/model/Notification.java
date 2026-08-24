package solutis.lucas.afonso.helpdesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import solutis.lucas.afonso.helpdesk.dtos.NotificationDTO;

import java.time.LocalDateTime;

@Entity(name = "notifications")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	private Long ticketId;
	private Long customerId;
	private Long technicianId;
	private String status;
	private String message;
	private LocalDateTime createdAt;

	public Notification() {
	}

	public Notification(EventType eventType, Long ticketId, Long customerId, Long technicianId, String status, String message, LocalDateTime createdAt) {
		this.eventType = eventType;
		this.ticketId = ticketId;
		this.customerId = customerId;
		this.technicianId = technicianId;
		this.status = status;
		this.message = message;
		this.createdAt = LocalDateTime.now();
	}

	public Notification(NotificationDTO notificationDTO) {
		this.eventType = notificationDTO.eventType();
		this.ticketId = notificationDTO.ticketId();
		this.customerId = notificationDTO.customerId();
		this.technicianId = notificationDTO.technicianId();
		this.status = notificationDTO.status();
		this.message = notificationDTO.message();
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Long technicianId) {
		this.technicianId = technicianId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}