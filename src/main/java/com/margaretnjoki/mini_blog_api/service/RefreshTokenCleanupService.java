package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RefreshTokenCleanupService {
    private final RefreshTokenRepository repository;

    public RefreshTokenCleanupService(RefreshTokenRepository repository) {
        this.repository = repository;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredRefreshTokens(){
        repository.deleteExpiredTokens(Instant.now());
    }
}

