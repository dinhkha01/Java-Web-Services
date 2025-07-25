package com.example.demo.repository;

import com.example.demo.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByAccountIdOrderByCreatedAtDesc(UUID accountId );
}
