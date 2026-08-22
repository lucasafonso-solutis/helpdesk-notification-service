package solutis.lucas.afonso.helpdesk.services;

import solutis.lucas.afonso.helpdesk.repository.NotificationRepository;
import solutis.lucas.afonso.helpdesk.dtos.NotificationDTO;
import solutis.lucas.afonso.helpdesk.model.Notification;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	private final NotificationRepository notificationRepository;

	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	public NotificationDTO create(NotificationDTO notificationDTO) {
		Notification notification = new Notification(notificationDTO);
		notification = notificationRepository.save(notification);

		return new NotificationDTO(notification);
	}

	public List<NotificationDTO> findAll() {
		return this.notificationRepository.findAll().stream().map(NotificationDTO::new).toList();
	}

	public List<NotificationDTO> findById(Long id) {
		return this.notificationRepository.findById(id).stream().map(NotificationDTO::new).toList();
	}
}