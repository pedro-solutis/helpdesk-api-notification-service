package br.com.solutis.helpdesk.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.solutis.helpdesk.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

    Page<Notification> findAllByRecipientId(Long recipientId, Pageable pageable);

    @Query(
        "select n from Notification n where " +
        "(:ticketId is null or n.ticketId = :ticketId) AND " +
        "(:recipientId is null or n.recipientId = :recipientId) AND " +
        "(:type is null or n.type like concat('%', :type, '%')) AND " +
        "(:read is null or n.read = :read)"
    )
    Page<Notification> findAllFilter(Long ticketId, Long recipientId, String type, Boolean read, Pageable pageable);

}
