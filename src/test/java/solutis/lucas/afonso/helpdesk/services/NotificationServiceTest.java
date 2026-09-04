package solutis.lucas.afonso.helpdesk.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import solutis.lucas.afonso.helpdesk.dtos.NotificationDTO;
import solutis.lucas.afonso.helpdesk.model.EventType;
import solutis.lucas.afonso.helpdesk.model.Notification;
import solutis.lucas.afonso.helpdesk.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        notificationDTO = new NotificationDTO(
                null, "message-1", EventType.TICKET_CREATED, 10L, 20L, null,
                "OPEN", "Ticket created", null);
    }

    @Test
    void createSavesNewNotificationWhenMessageIdDoesNotExist() {
        Notification savedNotification = new Notification(notificationDTO);
        savedNotification.setId(1L);
        when(notificationRepository.findByMessageId("message-1")).thenReturn(Optional.empty());
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        NotificationDTO result = notificationService.create(notificationDTO);

        assertEquals(1L, result.id());
        assertEquals(EventType.TICKET_CREATED, result.eventType());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createReturnsExistingNotificationWhenMessageIdAlreadyExists() {
        Notification existingNotification = new Notification(notificationDTO);
        existingNotification.setId(1L);
        when(notificationRepository.findByMessageId("message-1")).thenReturn(Optional.of(existingNotification));

        NotificationDTO result = notificationService.create(notificationDTO);

        assertEquals(1L, result.id());
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void createReturnsExistingAssignmentForSameTicketAndTechnician() {
        NotificationDTO assignmentDTO = new NotificationDTO(
                null, "message-2", EventType.TICKET_ASSIGNED, 10L, 20L, 30L,
                "IN_PROGRESS", "Technician assigned", null);
        Notification existingNotification = new Notification(assignmentDTO);
        existingNotification.setId(2L);
        when(notificationRepository.findFirstByEventTypeAndTicketIdAndTechnicianId(
                EventType.TICKET_ASSIGNED, 10L, 30L)).thenReturn(Optional.of(existingNotification));

        NotificationDTO result = notificationService.create(assignmentDTO);

        assertEquals(2L, result.id());
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(notificationRepository, never()).findByMessageId("message-2");
    }
}