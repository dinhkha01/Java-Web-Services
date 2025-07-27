package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.Account;
import com.example.demo.model.entity.Notification;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    public DataResponse<List<Notification>> getNotificationsByAccountId(Long accountId) throws NotFoundException {
        if (!accountRepository.existsById(accountId)) {
            throw new NotFoundException("Không tìm thấy tài khoản với ID: " + accountId);
        }

        List<Notification> notifications = notificationRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        return DataResponse.<List<Notification>>builder()
                .code(200)
                .key("notifications")
                .data(notifications)
                .build();
    }

    public void createNotification(Account account, String content) {
        Notification notification = new Notification();
        notification.setAccount(account);
        notification.setContent(content);
        notification.setStatus("chưa đọc");
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
        log.info("Đã tạo thông báo cho tài khoản {}: {}", account.getId(), content);
    }
}