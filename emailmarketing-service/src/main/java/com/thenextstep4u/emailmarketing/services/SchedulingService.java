package com.thenextstep4u.emailmarketing.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

public interface SchedulingService {
    @Transactional
    void sendMarketingEmails();
}
