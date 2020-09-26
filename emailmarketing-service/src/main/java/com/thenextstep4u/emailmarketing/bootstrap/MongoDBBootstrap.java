package com.thenextstep4u.emailmarketing.bootstrap;

import com.thenextstep4u.emailmarketing.model.Email;
import com.thenextstep4u.emailmarketing.model.EmailVariable;
import com.thenextstep4u.emailmarketing.model.MailingList;
import com.thenextstep4u.emailmarketing.repositories.EmailRepository;
import com.thenextstep4u.emailmarketing.repositories.MailingListRepository;
import com.thenextstep4u.emailmarketing.util.EmailType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Profile({"dev","prod","test"})
@Component
public class MongoDBBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private EmailRepository emailRepository;
    private MailingListRepository mailingListRepository;

    @Value("${thenextstep4u.web.url}")
    private String thenextstep4uWebURL;

    public MongoDBBootstrap(EmailRepository emailRepository, MailingListRepository mailingListRepository) {
        this.emailRepository = emailRepository;
        this.mailingListRepository = mailingListRepository;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        MailingList nextStepMailingList = mailingListRepository.findByName("NextStepSubscriptionList");
        if (nextStepMailingList == null) {
            nextStepMailingList = new MailingList();
            nextStepMailingList.setName("NextStepSubscriptionList");
            nextStepMailingList.setSystemName("The Next Step 4 U");
            nextStepMailingList.setLeadMagnetFilename("5StepsToBecomeSoftwareDeveloper.pdf");
            nextStepMailingList.setDomainUrl(thenextstep4uWebURL);
            nextStepMailingList.setLogoURL(thenextstep4uWebURL + "/images/tns4u_logo.png");
            mailingListRepository.save(nextStepMailingList);
        }
        Email welcomeEmail = emailRepository.findByEmailTypeAndMailingListId(EmailType.LEADMAGNET.value(), nextStepMailingList.getId());
        if (welcomeEmail == null) {
            welcomeEmail = new Email();
            welcomeEmail.setActive(true);
            welcomeEmail.setEmailName("Next Step Subscriber Lead magnet");
            welcomeEmail.setEmailType(EmailType.LEADMAGNET.value());
            welcomeEmail.setSubject("5 Steps to become a Software Developer");
            welcomeEmail.setMailingListId(nextStepMailingList.getId());
            welcomeEmail.setUnitNr(0);
            welcomeEmail.setUnitType("DAY");
            welcomeEmail.setTemplateName("subscriberwelcome");
            welcomeEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            welcomeEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");
            emailRepository.save(welcomeEmail);
        }

        Email rockyStartEmail = emailRepository.findByEmailTypeAndMailingListIdAndEmailName(EmailType.AUTORESPONDER.value(),
                nextStepMailingList.getId(), "A Rocky Start");
        if (rockyStartEmail == null) {
            rockyStartEmail = new Email();
            rockyStartEmail.setActive(true);
            rockyStartEmail.setEmailName("A Rocky Start");
            rockyStartEmail.setEmailType(EmailType.AUTORESPONDER.value());
            rockyStartEmail.setSubject("A rocky start ...");
            rockyStartEmail.setMailingListId(nextStepMailingList.getId());
            rockyStartEmail.setUnitNr(1);
            rockyStartEmail.setUnitType("DAY");
            rockyStartEmail.setTemplateName("arockystart");
            rockyStartEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            rockyStartEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");

            EmailVariable variable2 = new EmailVariable();
            variable2.setName("linkURL");
            variable2.setValue("/web/blog/arockystart/");
            rockyStartEmail.getEmailVariables().add(variable2);

            emailRepository.save(rockyStartEmail);
        }

        Email learnByDoingEmail = emailRepository.findByEmailTypeAndMailingListIdAndEmailName(EmailType.AUTORESPONDER.value(),
                nextStepMailingList.getId(), "Learn By Doing");
        if (learnByDoingEmail == null) {
            learnByDoingEmail = new Email();
            learnByDoingEmail.setActive(true);
            learnByDoingEmail.setEmailName("Learn By Doing");
            learnByDoingEmail.setEmailType(EmailType.AUTORESPONDER.value());
            learnByDoingEmail.setSubject("Learn to do by doing");
            learnByDoingEmail.setMailingListId(nextStepMailingList.getId());
            learnByDoingEmail.setUnitNr(2);
            learnByDoingEmail.setUnitType("DAY");
            learnByDoingEmail.setTemplateName("learnbydoing");
            learnByDoingEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            learnByDoingEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");

            EmailVariable variable3 = new EmailVariable();
            variable3.setName("linkURL");
            variable3.setValue("/web/blog/learnbydoing/");
            learnByDoingEmail.getEmailVariables().add(variable3);


            emailRepository.save(learnByDoingEmail);
        }

        Email planYourCodeEmail = emailRepository.findByEmailTypeAndMailingListIdAndEmailName(EmailType.AUTORESPONDER.value(),
                nextStepMailingList.getId(), "Plan Your Code, Code Your Plan");
        if (planYourCodeEmail == null) {
            planYourCodeEmail = new Email();
            planYourCodeEmail.setActive(true);
            planYourCodeEmail.setEmailName("Plan Your Code, Code Your Plan");
            planYourCodeEmail.setEmailType(EmailType.AUTORESPONDER.value());
            planYourCodeEmail.setSubject("Plan Your Code, Code Your Plan");
            planYourCodeEmail.setMailingListId(nextStepMailingList.getId());
            planYourCodeEmail.setUnitNr(4);
            planYourCodeEmail.setUnitType("DAY");
            planYourCodeEmail.setTemplateName("planyourcode");
            planYourCodeEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            planYourCodeEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");

            EmailVariable variable = new EmailVariable();
            variable.setName("linkURL");
            variable.setValue("/web/blog/planyourcode/");
            planYourCodeEmail.getEmailVariables().add(variable);

            emailRepository.save(planYourCodeEmail);
        }

        Email whyJavaEmail = emailRepository.findByEmailTypeAndMailingListIdAndEmailName(EmailType.AUTORESPONDER.value(),
                nextStepMailingList.getId(), "Why Java?");
        if (whyJavaEmail == null) {
            whyJavaEmail = new Email();
            whyJavaEmail.setActive(true);
            whyJavaEmail.setEmailName("Why Java?");
            whyJavaEmail.setEmailType(EmailType.AUTORESPONDER.value());
            whyJavaEmail.setSubject("Why Learn Java?");
            whyJavaEmail.setMailingListId(nextStepMailingList.getId());
            whyJavaEmail.setUnitNr(6);
            whyJavaEmail.setUnitType("DAY");
            whyJavaEmail.setTemplateName("whyjava");
            whyJavaEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            whyJavaEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");

            EmailVariable variable = new EmailVariable();
            variable.setName("linkURL");
            variable.setValue("/web/blog/whyjava/");
            whyJavaEmail.getEmailVariables().add(variable);
            emailRepository.save(whyJavaEmail);
        }

        Email whyHTMLEmail = emailRepository.findByEmailTypeAndMailingListIdAndEmailName(EmailType.AUTORESPONDER.value(),
                nextStepMailingList.getId(), "Why HTML?");
        if (whyHTMLEmail == null) {
            whyHTMLEmail = new Email();
            whyHTMLEmail.setActive(true);
            whyHTMLEmail.setEmailName("Why HTML?");
            whyHTMLEmail.setEmailType(EmailType.AUTORESPONDER.value());
            whyHTMLEmail.setSubject("Why Learn HTML, CSS and Bootstrap?");
            whyHTMLEmail.setMailingListId(nextStepMailingList.getId());
            whyHTMLEmail.setUnitNr(9);
            whyHTMLEmail.setUnitType("DAY");
            whyHTMLEmail.setTemplateName("whyhtml");
            whyHTMLEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            whyHTMLEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");

            EmailVariable variable = new EmailVariable();
            variable.setName("linkURL");
            variable.setValue("/web/blog/whyhtml/");
            whyHTMLEmail.getEmailVariables().add(variable);
            emailRepository.save(whyHTMLEmail);
        }

        Email springFrameworkEmail = emailRepository.findByEmailTypeAndMailingListIdAndEmailName(EmailType.AUTORESPONDER.value(),
                nextStepMailingList.getId(), "What is Spring Framework?");
        if (springFrameworkEmail == null) {
            springFrameworkEmail = new Email();
            springFrameworkEmail.setActive(true);
            springFrameworkEmail.setEmailName("What is Spring Framework?");
            springFrameworkEmail.setEmailType(EmailType.AUTORESPONDER.value());
            springFrameworkEmail.setSubject("What is Spring Framework?");
            springFrameworkEmail.setMailingListId(nextStepMailingList.getId());
            springFrameworkEmail.setUnitNr(12);
            springFrameworkEmail.setUnitType("DAY");
            springFrameworkEmail.setTemplateName("springframework");
            springFrameworkEmail.setFromAddress("liezelshaw@thenextstep4u.com");
            springFrameworkEmail.setReplyToAddress("liezelshaw@thenextstep4u.com");

            EmailVariable variable = new EmailVariable();
            variable.setName("linkURL");
            variable.setValue("/web/blog/springframework/");
            springFrameworkEmail.getEmailVariables().add(variable);
            emailRepository.save(springFrameworkEmail);
        }
    }
}
