package solutis.lucas.afonso.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import solutis.lucas.afonso.helpdesk.model.Notification;
import solutis.lucas.afonso.helpdesk.model.EventType;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Optional<Notification> findByMessageId(String messageId);

	Optional<Notification> findFirstByEventTypeAndTicketIdAndTechnicianId(
			EventType eventType, Long ticketId, Long technicianId);

}