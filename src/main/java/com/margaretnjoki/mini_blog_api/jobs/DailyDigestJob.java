package com.margaretnjoki.mini_blog_api.jobs;

import com.margaretnjoki.mini_blog_api.service.DigestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyDigestJob {

    private final DigestService digestService;
    public DailyDigestJob(DigestService digestService) {
        this.digestService = digestService;
    }

    @Scheduled(cron = "0 0 8 * * *") // Runs every day at 9 AM
    public void runDailyDigest() {
        digestService.sendDailyDigest();
    }
}