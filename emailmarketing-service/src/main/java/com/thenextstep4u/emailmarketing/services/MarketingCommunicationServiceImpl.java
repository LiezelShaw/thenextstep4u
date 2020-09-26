package com.thenextstep4u.emailmarketing.services;

import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetDTO;
import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetEmailDTO;
import com.thenextstep4u.emailmarketing.exceptions.DoesNotExistException;
import com.thenextstep4u.emailmarketing.model.*;
import com.thenextstep4u.emailmarketing.repositories.*;
import com.thenextstep4u.emailmarketing.util.EmailSubscriberStatus;
import com.thenextstep4u.emailmarketing.util.EmailType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketingCommunicationServiceImpl implements MarketingCommunicationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Value("${thenextstep4u.gateway.url}")
//    private String gatewayUrl;

    @Value("${thenextstep4u.web.url}")
    private String webDomainUrl;

    @Value("${thenextstep4u.emailmarketing.path.unsubscribe}")
    private String unsubscribePath;

    @Value("${thenextstep4u.emailmarketing.path.leadmagnet}")
    private String leadMagnetPath;

    private EmailSubscriberRepository emailSubscriberRepository;
    private EmailService emailService;
    private EmailRepository emailRepository;
    private MailingListRepository mailingListRepository;
    private MailingListSubscriptionRepository mailingListSubscriptionRepository;
    private SentEmailInfoRepository sentEmailInfoRepository;

    public MarketingCommunicationServiceImpl(EmailSubscriberRepository emailSubscriberRepository, EmailService emailService,
                                             EmailRepository emailRepository, MailingListRepository mailingListRepository,
                                             MailingListSubscriptionRepository mailingListSubscriptionRepository,
                                             SentEmailInfoRepository sentEmailInfoRepository) {
        this.emailSubscriberRepository = emailSubscriberRepository;
        this.emailService = emailService;
        this.emailRepository = emailRepository;
        this.sentEmailInfoRepository = sentEmailInfoRepository;
        this.mailingListRepository = mailingListRepository;
        this.mailingListSubscriptionRepository = mailingListSubscriptionRepository;
    }

    @Override
    @Transactional
    public void saveEmailAndSendWelcomeDoc(LeadMagnetEmailDTO leadMagnetEmail) throws DoesNotExistException {
        MailingList mailingList = mailingListRepository.findByName(leadMagnetEmail.getMailingListName());
        if (mailingList == null) {
            throw new DoesNotExistException("The mailing list does not exist: " + leadMagnetEmail.getMailingListName());
        }
        List<Email> emails = emailRepository.findByEmailTypeAndMailingListIdAndEmailType(mailingList.getId(), EmailType.LEADMAGNET.value());
        Email email = emails.get(0);
        if (email == null) {
            throw new DoesNotExistException("The requested email does not exist: " + EmailType.LEADMAGNET.value() + " : "
                    + leadMagnetEmail.getMailingListName());
        }
        EmailSubscriber subscriber = emailSubscriberRepository.findByEmail(leadMagnetEmail.getEmail());
        if (subscriber == null) {
            subscriber = new EmailSubscriber();
            subscriber.setEmail(leadMagnetEmail.getEmail());
            subscriber.setClientName(leadMagnetEmail.getClientName());
            emailSubscriberRepository.save(subscriber);
        }
        MailingListSubscription mailingListSubscription = mailingListSubscriptionRepository.
                findByMailingListIdAndEmailSubscriberId(mailingList.getId(), subscriber.getId());
        if (mailingListSubscription == null) {
            mailingListSubscription = new MailingListSubscription();
            mailingListSubscription.setDateTimeSubscribed(LocalDateTime.now());
            mailingListSubscription.setStatus(EmailSubscriberStatus.PENDING.value());
            mailingListSubscription.setEmailSubscriberId(subscriber.getId());
            mailingListSubscription.setMailingListId(mailingList.getId());
            mailingListSubscriptionRepository.save(mailingListSubscription);
        }
        try {
            sendSubscriberWelcomeEmail(mailingList, mailingListSubscription, email);
            createSentEmailInfo(mailingListSubscription,email);
            mailingListSubscriptionRepository.save(mailingListSubscription);
        }  catch (MessagingException ex) {
            logger.error("Subscriber welcome e-mail could not be sent: ", ex);
            mailingListSubscription.setStatus(EmailSubscriberStatus.INVALID.value());
            mailingListSubscription.setStatusReason(ex.getMessage());
            mailingListSubscriptionRepository.save(mailingListSubscription);
        }
    }

    private void createSentEmailInfo(MailingListSubscription mailingListSubscription, Email email) {
        SentEmailInfo sentEmailInfo = sentEmailInfoRepository.findByEmailIdAndMailingListSubscriptionId(email.getId(), mailingListSubscription.getId());
        if (sentEmailInfo == null) {
            sentEmailInfo = new SentEmailInfo();
            sentEmailInfo.setEmailId(email.getId());
            sentEmailInfo.setMailingListSubscriptionId(mailingListSubscription.getId());
            sentEmailInfoRepository.save(sentEmailInfo);
        }
    }

    /**
     * Sending the Subscriber welcome email.
     * The following variables will automatically be added to the context for the email:
     *      - unsubscribeURL
     *      - emailSubscriber (email address)
     *      - emailSubscriberId (id of email subscriber)
     *      - domainURL (the domain url for the next step)
     * @param mailingListSubscription
     * @param email
     * @throws MessagingException
     */
    private void sendSubscriberWelcomeEmail(MailingList mailingList, MailingListSubscription mailingListSubscription, Email email) throws MessagingException {
        logger.debug("Preparing to send registration confirmation email ...");
        EmailSubscriber emailSubscriber = emailSubscriberRepository.findById(mailingListSubscription.getEmailSubscriberId()).get();
        Context ctx = getEmailContext(mailingList, email,mailingListSubscription);
        ctx.setVariable("leadMagnetURL", webDomainUrl + leadMagnetPath + mailingListSubscription.getId());
        emailService.sendEmail(emailSubscriber.getEmail(), email.getSubject(), ctx,
                mailingList.getName() + "/" + email.getTemplateName(), email.getFromAddress(),email.getReplyToAddress());
        logger.debug("Registration confirmation email sent!");
    }

    private  Context getEmailContext(MailingList mailingList, Email email, MailingListSubscription mailingListSubscription) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        email.getEmailVariables().stream().forEach(variable -> ctx.setVariable(variable.getName(),variable.getValue()));
        ctx.setVariable("unsubscribeURL", getUnsubscribeURL(mailingListSubscription.getId()));
        ctx.setVariable("emailSubscriber", emailSubscriberRepository.findById(mailingListSubscription.getEmailSubscriberId()).get().getEmail());
        ctx.setVariable("mailingListSubscriptionId", mailingListSubscription.getId());
        ctx.setVariable("emailId", email.getId());
        ctx.setVariable("logoURL", mailingList.getLogoURL());
        ctx.setVariable("webDomainUrl", webDomainUrl);
        return ctx;
    }

    /**
     * Send weekly emails to subscribers who have been subscribed for 1 week
     * or more and haven't received it yet
     */
    @Override
    public void  sendAutoResponderEmails() {
        try {
            mailingListRepository.findAll().forEach(mailingList -> {
                emailRepository.findByEmailTypeAndMailingListIdAndEmailType(mailingList.getId(), EmailType.AUTORESPONDER.value()).forEach(email -> {
                    if (email.isActive()) {
                        String unitType = email.getUnitType();
                        int unitNumber = email.getUnitNr();
                        LocalDateTime emailDate = LocalDateTime.now();
                        if (unitType.equals("WEEK")) {
                            emailDate = emailDate.minusWeeks(unitNumber);
                        } else if (unitType.equals("DAY")) {
                            emailDate = emailDate.minusDays(unitNumber);
                        } else if (unitType.equals("MONTH")) {
                            emailDate = emailDate.minusMonths(unitNumber);
                        } else {
                            logger.error("Unit type has not been set for email " + email.getEmailName());
//                            auditService.updateAuditWithFailure(audit, "Unit type has not been set", emailToSubscribers);
                            return;
                        }
                        logger.debug("Email Date:  " + emailDate);
                        mailingListSubscriptionRepository.findByMailingListIdAndDateTimeSubscribedBefore(mailingList.getId(),
                                emailDate).forEach(mailingListSubscription -> {
                            if (!mailingListSubscription.getStatus().equals(EmailSubscriberStatus.INVALID.value()) &&
                                    !mailingListSubscription.getStatus().equals(EmailSubscriberStatus.UNSUBSCRIBED.value())) {
                                if (sentEmailInfoRepository.findByEmailIdAndMailingListSubscriptionId(email.getId(),mailingListSubscription.getId()) == null) {
                                    EmailSubscriber subscriber = emailSubscriberRepository.findById(mailingListSubscription.getEmailSubscriberId()).get();
                                    logger.info("Email Subscriber: " + subscriber.getEmail());
                                    try {
                                        emailService.sendEmail(subscriber.getEmail(), email.getSubject(), getEmailContext(mailingList, email, mailingListSubscription),
                                                mailingList.getName() + "/" + email.getTemplateName(), email.getFromAddress(),email.getReplyToAddress());
                                        createSentEmailInfo(mailingListSubscription,email);
                                        mailingListSubscriptionRepository.save(mailingListSubscription);
                                    } catch (MessagingException e) {
                                        logger.error(e.getMessage(), e);
                                        //log exception with audit
                                    }
                                }
                            }
                        });
                    }
                });
            });
//            auditService.updateAuditWithSuccess(audit);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//            auditService.updateAuditWithFailure(audit, e.getMessage(), e);
        }
    }

    /**
     * Send weekly emails to subscribers who have been subscribed for 1 week
     * or more and haven't received it yet
     */
    @Override
    public void  sendWeeklyBlogEmails() {
        try {
            mailingListRepository.findAll().forEach(mailingList -> {
                emailRepository.findByMailingListId(mailingList.getId()).forEach(email -> {
                    if (email.isActive()) {
                        String unitType = email.getUnitType();
                        int unitNumber = email.getUnitNr();
                        LocalDateTime emailDate = LocalDateTime.now();
                        if (unitType.equals("WEEK")) {
                            emailDate = emailDate.minusWeeks(unitNumber);
                        } else if (unitType.equals("DAY")) {
                            emailDate = emailDate.minusDays(unitNumber);
                        } else if (unitType.equals("MONTH")) {
                            emailDate = emailDate.minusMonths(unitNumber);
                        } else {
                            logger.error("Unit type has not been set for email " + email.getEmailName());
//                            auditService.updateAuditWithFailure(audit, "Unit type has not been set", emailToSubscribers);
                            return;
                        }
                        logger.debug("Email Date:  " + emailDate);
                        mailingListSubscriptionRepository.findByMailingListIdAndDateTimeSubscribedBefore(mailingList.getId(),
                                emailDate).forEach(mailingListSubscription -> {
                            if (!mailingListSubscription.getStatus().equals(EmailSubscriberStatus.INVALID.value()) &&
                                    !mailingListSubscription.getStatus().equals(EmailSubscriberStatus.UNSUBSCRIBED.value())) {
                                if (sentEmailInfoRepository.findByEmailIdAndMailingListSubscriptionId(email.getId(),mailingListSubscription.getId()) == null) {
                                    EmailSubscriber subscriber = emailSubscriberRepository.findById(mailingListSubscription.getEmailSubscriberId()).get();
                                    logger.info("Email Subscriber: " + subscriber.getEmail());
                                    try {
                                        emailService.sendEmail(subscriber.getEmail(), email.getSubject(), getEmailContext(mailingList, email, mailingListSubscription),
                                                mailingList.getName() + "/" + email.getTemplateName(), email.getFromAddress(),email.getReplyToAddress());
                                        createSentEmailInfo(mailingListSubscription,email);
                                        mailingListSubscriptionRepository.save(mailingListSubscription);
                                    } catch (MessagingException e) {
                                        logger.error(e.getMessage(), e);
                                        //log exception with audit
                                    }
                                }
                            }
                        });
                    }
                });
            });
//            auditService.updateAuditWithSuccess(audit);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//            auditService.updateAuditWithFailure(audit, e.getMessage(), e);
        }
    }

    private String getUnsubscribeURL(String mailingListSubscriptionId) {
        StringBuilder unsubscribeURL = new StringBuilder(webDomainUrl);
        unsubscribeURL.append(unsubscribePath).append(mailingListSubscriptionId);
        return unsubscribeURL.toString();
    }

    @Override
    @Transactional
    public LeadMagnetDTO getLeadMagnet(String mailingListSubscriptionId) throws DoesNotExistException {
        LeadMagnetDTO leadMagnetDTO = new LeadMagnetDTO();
        MailingListSubscription subscription = mailingListSubscriptionRepository.findById(mailingListSubscriptionId).get();
        subscription.setStatus(EmailSubscriberStatus.VERIFIED.value());
        MailingList mailingList = mailingListRepository.findById(subscription.getMailingListId()).get();
        String leadMagnetFilename = "mailRepository/" + mailingList.getName() + "/" + mailingList.getLeadMagnetFilename();
        leadMagnetDTO.setLeadmagnetName(mailingList.getLeadMagnetFilename());
        subscription.setStatusReason(leadMagnetFilename + " downloaded!!");
        mailingListSubscriptionRepository.save(subscription);
        List<Email> emails = emailRepository.findByEmailTypeAndMailingListIdAndEmailType(mailingList.getId(), EmailType.LEADMAGNET.value());
        Email email = emails.get(0);
        if (email == null) {
            throw new DoesNotExistException("The requested email does not exist: " + EmailType.LEADMAGNET.value() + " : "
                    + mailingList.getName());
        }
        setEmailLinkClickedForSubscription(email, subscription);
        try {
            ByteArrayOutputStream byteArrayOutputStream = loadLeadMagnet(leadMagnetFilename);
            String pdfBase64String = StringUtils.newStringUtf8(Base64.encodeBase64(byteArrayOutputStream.toByteArray()));
            leadMagnetDTO.setLeadmagnetFile(pdfBase64String);
        } catch (IOException ex) {
            logger.error("Retrieving Lead magnet for subscriber was not successful: ", ex);
            throw new DoesNotExistException(ex.getMessage(), ex);
        }
        return leadMagnetDTO;
    }

    private ByteArrayOutputStream loadLeadMagnet(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            outputStream.write(buf, 0, readNum); //no doubt here is 0
        }
        return outputStream;
    }

    private void setEmailLinkClickedForSubscription(Email email, MailingListSubscription mailingListSubscription) {
        SentEmailInfo sentEmailInfo = sentEmailInfoRepository.findByEmailIdAndMailingListSubscriptionId(email.getId(),mailingListSubscription.getId());
        sentEmailInfo.setLinkClicked(true);
        sentEmailInfoRepository.save(sentEmailInfo);
    }

    @Override
    public String findLeadMagnetName(String mailingListSubscriptionId) {
        MailingListSubscription subscription = mailingListSubscriptionRepository.findById(mailingListSubscriptionId).get();
        MailingList mailingList = mailingListRepository.findById(subscription.getMailingListId()).get();
        return mailingList.getLeadMagnetFilename();
    }

    @Override
    public void unsubscribe(String mailingListSubscriptionId) {
        MailingListSubscription mailingListSubscription = mailingListSubscriptionRepository.findById(mailingListSubscriptionId).get();
        mailingListSubscription.setStatus(EmailSubscriberStatus.UNSUBSCRIBED.value());
        mailingListSubscriptionRepository.save(mailingListSubscription);
    }

    @Override
    public void setEmailLinkClickedForSubscriber(String mailingListSubscriptionId, String emailId) {
        MailingListSubscription mailingListSubscription = mailingListSubscriptionRepository.findById(mailingListSubscriptionId).get();
        Email email = emailRepository.findById(emailId).get();
        setEmailLinkClickedForSubscription(email,mailingListSubscription);
    }

}
