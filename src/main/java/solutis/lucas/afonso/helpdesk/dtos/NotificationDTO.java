package solutis.lucas.afonso.helpdesk.dtos;

import java.time.LocalDateTime;

import solutis.lucas.afonso.helpdesk.model.Notification;

public record NotificationDTO(Long id, String eventType, Long ticketId, Long customerId, Long technicianId, String status, 
                                String message, LocalDateTime createdAt) {

    public NotificationDTO(Notification notification) {
        this(notification.getId(), notification.getEventType(), notification.getTicketId(), notification.getCustomerId(),
                notification.getTechnicianId(), notification.getStatus(), notification.getMessage(), notification.getCreatedAt());
    }                                
}
