package br.com.solutis.helpdesk.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.dto.NotificationListDTO;
import br.com.solutis.helpdesk.notification.service.NotificationService;

@RestController
@RequestMapping ("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationListDTO>> getAllNotifications(Pageable pageable){
        var notifications = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<NotificationDetailDTO> getNotificationById(@PathVariable("id") Long notificationId){
        var notification = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(notification);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<String> readNotification(@PathVariable("id") Long notifcationId){
        notificationService.readNotification(notifcationId);
        return ResponseEntity.ok("Marked as read");
    }

}
