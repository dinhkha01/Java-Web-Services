package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.Notification;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{accountId}")
    public ResponseEntity<DataResponse<List<Notification>>> getNotificationsByAccountId(@PathVariable UUID accountId) throws NotFoundException {
        return new ResponseEntity<>(notificationService.getNotificationsByAccountId(accountId), HttpStatus.OK);
    }
}