package com.thenextstep4u.emailmarketing.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailSchedulingService implements SchedulingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MarketingCommunicationService marketingCommunicationService;

    @Autowired
    public EmailSchedulingService(MarketingCommunicationService marketingCommunicationService) {
        this.marketingCommunicationService = marketingCommunicationService;
    }

    @Transactional
    @Scheduled(cron = "0 52 11 * * ?")
    @Override
    public void sendMarketingEmails() {
        logger.info("Email Scheduler called! " + LocalDateTime.now());
        marketingCommunicationService.sendAutoResponderEmails();
    }
}
