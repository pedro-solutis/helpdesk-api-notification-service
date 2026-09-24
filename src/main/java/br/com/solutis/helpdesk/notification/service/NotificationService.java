package br.com.solutis.helpdesk.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.dto.NotificationListDTO;
import br.com.solutis.helpdesk.notification.dto.TicketEventDTO;
import br.com.solutis.helpdesk.notification.exception.ReadNotificationException;
import br.com.solutis.helpdesk.notification.exception.ResourceNotFoundException;
import br.com.solutis.helpdesk.notification.model.Notification;
import br.com.solutis.helpdesk.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;

@Service 
public class NotificationService {

    @Autowired 
    private NotificationRepository notificationRepository;

    public Page<NotificationListDTO> getAllNotifications(Long ticketId, Long recipientId, String type, Boolean read, Pageable pageable) {    
        var notifications = notificationRepository.findAllFilter(
            ticketId != null ? ticketId : null,
            recipientId != null ? recipientId : null,
            type != null ? type : null,
            read != null ? read : null,
            pageable);
        return notifications.map(NotificationListDTO::new);
    }

    public NotificationDetailDTO getNotificationById(Long notificationId) {
        var notification = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        return new NotificationDetailDTO(notification);
    }

    @Transactional
    public void processTicketEvent(TicketEventDTO event) {
        var notification =  new Notification(event);
        notificationRepository.save(notification);
    }

    public void readNotification(Long notifcationId) {
        var notification = notificationRepository.findById(notifcationId).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if(notification.isRead())
            throw new ReadNotificationException("Notification was read before");
        notification.read();
        notificationRepository.save(notification);
    }

}
