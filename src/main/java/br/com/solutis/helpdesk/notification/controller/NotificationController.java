package br.com.solutis.helpdesk.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.solutis.helpdesk.notification.service.NotificationService;

@RestController
@RequestMapping ("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public void getAllNotifications(){
        notificationService.getAllNotifications();
    }

    @GetMapping ("/{id}")
    private void getNotificationById(@PathVariable("id") Long notificationId){
        notificationService.getNotificationById(notificationId);
    }

}
