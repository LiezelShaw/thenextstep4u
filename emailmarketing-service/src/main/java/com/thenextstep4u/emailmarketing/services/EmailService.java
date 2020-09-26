package com.thenextstep4u.emailmarketing.services;

import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(String email, String subject, Context ctx, String templateName, String fromAddress, String replyToAddress) throws MessagingException;

}
