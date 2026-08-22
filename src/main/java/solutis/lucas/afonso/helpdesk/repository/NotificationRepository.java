package solutis.lucas.afonso.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import solutis.lucas.afonso.helpdesk.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}