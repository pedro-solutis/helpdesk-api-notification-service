package br.com.solutis.helpdesk.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.solutis.helpdesk.notification.repository.NotificationRepository;

@Service 
public class NotificationService {

    @Autowired 
    private NotificationRepository notificationRepository;

    public void getAllNotifications() {
        notificationRepository.findAll();
    }

    public void getNotificationById(Long notificationId) {
        notificationRepository.findById(notificationId);
    }

}
