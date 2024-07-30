package com.ead.notification.services.impl;

import com.ead.notification.repositories.NotificationRepository;
import com.ead.notification.services.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
}
