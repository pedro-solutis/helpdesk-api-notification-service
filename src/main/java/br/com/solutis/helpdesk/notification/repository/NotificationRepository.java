package br.com.solutis.helpdesk.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.solutis.helpdesk.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

}
