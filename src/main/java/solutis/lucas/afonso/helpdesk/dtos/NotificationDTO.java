package solutis.lucas.afonso.helpdesk.dtos;

import java.time.LocalDateTime;

import solutis.lucas.afonso.helpdesk.model.EventType;
import solutis.lucas.afonso.helpdesk.model.Notification;

public record NotificationDTO(Long id, String messageId, EventType eventType, Long ticketId, Long customerId, Long technicianId, String status, 
    String message, LocalDateTime createdAt) {

    public NotificationDTO(Notification notification) {
        this(notification.getId(), notification.getMessageId(), notification.getEventType(), notification.getTicketId(), notification.getCustomerId(),
            notification.getTechnicianId(), notification.getStatus(), notification.getMessage(), notification.getCreatedAt());
    }                                
}
