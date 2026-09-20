package br.com.solutis.helpdesk.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.dto.NotificationListDTO;
import br.com.solutis.helpdesk.notification.exception.ResourceNotFoundException;
import br.com.solutis.helpdesk.notification.repository.NotificationRepository;

@Service 
public class NotificationService {

    @Autowired 
    private NotificationRepository notificationRepository;

    public Page<NotificationListDTO> getAllNotifications(Pageable pageable) {
        var notifications = notificationRepository.findAll(pageable);
        return notifications.map(NotificationListDTO::new);
    }

    public NotificationDetailDTO getNotificationById(Long notificationId) {
        var ticket = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        return new NotificationDetailDTO(ticket.getType(), ticket.getMessage(), ticket.getTickedId(), ticket.getRecipientId());
    }

}
