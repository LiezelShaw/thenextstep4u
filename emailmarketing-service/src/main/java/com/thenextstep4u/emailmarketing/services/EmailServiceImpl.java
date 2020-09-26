package com.thenextstep4u.emailmarketing.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    @Qualifier("emailTemplateEngine")
    private TemplateEngine htmlTemplateEngine;


    @Override
    public void sendEmail(String email, String subject, Context ctx, String templateName, String fromAddress, String replyToAddress) throws MessagingException {
        // Create the HTML body using Thymeleaf
        final String htmlContent = htmlTemplateEngine.process(templateName, ctx);
        sendMessage(subject, email, htmlContent, fromAddress, replyToAddress);
    }

    private void sendMessage(String subject, String recipientEmail, String htmlContent, String fromAddress, String replyToAddress) throws MessagingException {
        logger.debug("Sending email ...");
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject(subject);
        message.setFrom(fromAddress);
        message.setReplyTo(replyToAddress);
        message.setTo(recipientEmail);
        message.setText(htmlContent, true /* isHtml */);

        // Send mail
        this.mailSender.send(mimeMessage);
    }



}
