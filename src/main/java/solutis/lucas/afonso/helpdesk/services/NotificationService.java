package solutis.lucas.afonso.helpdesk.services;

import solutis.lucas.afonso.helpdesk.repository.NotificationRepository;
import solutis.lucas.afonso.helpdesk.dtos.NotificationDTO;
import solutis.lucas.afonso.helpdesk.model.Notification;
import solutis.lucas.afonso.helpdesk.model.EventType;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	private final NotificationRepository notificationRepository;

	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	public NotificationDTO create(NotificationDTO notificationDTO) {
		if (notificationDTO.eventType() == EventType.TICKET_ASSIGNED) {
			return notificationRepository.findFirstByEventTypeAndTicketIdAndTechnicianId(
					notificationDTO.eventType(), notificationDTO.ticketId(), notificationDTO.technicianId())
				.map(NotificationDTO::new)
				.orElseGet(() -> save(notificationDTO));
		}
		if (notificationDTO.messageId() != null) {
			return notificationRepository.findByMessageId(notificationDTO.messageId())
				.map(NotificationDTO::new)
				.orElseGet(() -> save(notificationDTO));
		}
		return save(notificationDTO);
	}

	private NotificationDTO save(NotificationDTO notificationDTO) {
		Notification notification = new Notification(notificationDTO);
		notification = notificationRepository.save(notification);

		return new NotificationDTO(notification);
	}

	public List<NotificationDTO> findAll() {
		return this.notificationRepository.findAll().stream()
				.map(NotificationDTO::new)
				.collect(java.util.stream.Collectors.toMap(
						notification -> notification.eventType() == EventType.TICKET_ASSIGNED
							? notification.eventType() + ":" + notification.ticketId() + ":" + notification.technicianId()
							: notification.id().toString(),
						notification -> notification,
						(first, duplicate) -> first,
						java.util.LinkedHashMap::new))
				.values().stream().toList();
	}

	public List<NotificationDTO> findById(Long id) {
		return this.notificationRepository.findById(id).stream().map(NotificationDTO::new).toList();
	}
}