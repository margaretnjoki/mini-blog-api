package com.margaretnjoki.mini_blog_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendWelcomeNotification(String email) {
        log.info("sending welcome notification to {} on thread {}", email, Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("welcome notification sent to {}", email);
    }
}

