package br.com.solutis.helpdesk.notification.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter 
@AllArgsConstructor 
@NoArgsConstructor 
public class Notification {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long tickedId;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column (name = "read", nullable = false, columnDefinition = "DEFAULT FALSE")
    private boolean read = false;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp 
    private LocalDateTime createdAt;
    
}
