package com.thenextstep4u.emailmarketing.services;

import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetDTO;
import com.thenextstep4u.emailmarketing.exceptions.DoesNotExistException;
import com.thenextstep4u.emailmarketing.model.*;
import com.thenextstep4u.emailmarketing.repositories.*;
import com.thenextstep4u.emailmarketing.util.EmailSubscriberStatus;
import com.thenextstep4u.emailmarketing.util.EmailType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class MarketingCommunicationServiceImplTest {

    @InjectMocks
    MarketingCommunicationServiceImpl marketingCommunicationService;
    @Mock
    MailingListSubscriptionRepository mailingListSubscriptionRepository;
    @Mock
    MailingListRepository mailingListRepository;
    @Mock
    EmailRepository emailRepository;
    @Mock
    EmailSubscriberRepository emailSubscriberRepository;
    @Mock
    EmailService emailService;
    @Mock
    SentEmailInfoRepository sentEmailInfoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
//        marketingCommunicationService = new MarketingCommunicationServiceImpl(emailSubscriberRepository, emailService, emailRepository, mailingListRepository,
//                mailingListSubscriptionRepository, sentEmailInfoRepository);
    }

    private MailingListSubscription createMailingListSubscription() {
        String id = "1";
        MailingListSubscription subscription = new MailingListSubscription();
        subscription.setDateTimeSubscribed(LocalDateTime.now());
        subscription.setEmailSubscriberId(id);
        subscription.setId(id);
        subscription.setMailingListId(id);
        subscription.setStatus(EmailSubscriberStatus.VERIFIED.value());
        subscription.setStatusReason("This is a test");
        return subscription;
    }

    private MailingList createMailingList() {
        MailingList mailingList = new MailingList();
        mailingList.setId("1");
        mailingList.setName("NextStepSubscriptionList");
        mailingList.setSystemName("The Next Step 4 U");
        mailingList.setLeadMagnetFilename("5StepsToBecomeSoftwareDeveloper.pdf");
        mailingList.setDomainUrl("http://localhost:8080");
        mailingList.setLogoURL("/images/thenextstepicon.png");
        return mailingList;
    }

    private Email createEmail() {
        String id = "1";
        Email email = new Email();
        email.setId(id);
        email.setActive(true);
        email.setEmailName("Next Step Subscriber Lead magnet");
        email.setEmailType(EmailType.LEADMAGNET.value());
        email.setSubject("5 Steps to become a Software Developer");
        email.setMailingListId(id);
        email.setUnitNr(0);
        email.setUnitType("DAY");
        email.setTemplateName("subscriberwelcome");
        email.setFromAddress("liezelshaw@thenextstep4u.com");
        email.setReplyToAddress("liezelshaw@thenextstep4u.com");

        EmailVariable variable1 = new EmailVariable();
        variable1.setName("sendLeadURL");
        variable1.setValue("/api/v1/email/leadmagnet/");
        email.getEmailVariables().add(variable1);
        return email;
    }

    private SentEmailInfo createSentEmailInfo() {
        SentEmailInfo sentEmailInfo = new SentEmailInfo();
        String id = "1";
        sentEmailInfo.setId(id);
        sentEmailInfo.setEmailId(id);
        sentEmailInfo.setEmailOpened(true);
        sentEmailInfo.setLinkClicked(true);
        sentEmailInfo.setMailingListSubscriptionId(id);
        return sentEmailInfo;
    }

    @Test
    void getLeadMagnet() {
        try {
            String id = "1";
            MailingListSubscription subscription = createMailingListSubscription();
            MailingList mailingList = createMailingList();
            Email email = createEmail();
            when(mailingListSubscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));
            when(mailingListRepository.findById(subscription.getMailingListId())).thenReturn(Optional.of(mailingList));
            when(mailingListSubscriptionRepository.save(subscription)).thenReturn(null);
            when(emailRepository.findByEmailTypeAndMailingListId(EmailType.LEADMAGNET.value(), mailingList.getId())).thenReturn(email);
            when(sentEmailInfoRepository.findByEmailIdAndMailingListSubscriptionId(email.getId(),subscription.getId()))
                    .thenReturn(createSentEmailInfo());
            LeadMagnetDTO leadMagnetDTO = marketingCommunicationService.getLeadMagnet(id);
            String leadMagnetString = leadMagnetDTO.getLeadmagnetFile();
            String pdfBase64String = StringUtils.newStringUtf8(Base64.encodeBase64(FileUtil.readAsByteArray(
                    new File("mailRepository/NextStepSubscriptionList/5StepsToBecomeSoftwareDeveloper.pdf"))));
            assertEquals(pdfBase64String, leadMagnetString);
        } catch (DoesNotExistException | FileNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    private static boolean isEqual(InputStream i1, InputStream i2)
            throws IOException {

        ReadableByteChannel ch1 = Channels.newChannel(i1);
        ReadableByteChannel ch2 = Channels.newChannel(i2);

        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

        try {
            while (true) {

                int n1 = ch1.read(buf1);
                int n2 = ch2.read(buf2);

                if (n1 == -1 || n2 == -1) return n1 == n2;

                buf1.flip();
                buf2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++)
                    if (buf1.get() != buf2.get())
                        return false;

                buf1.compact();
                buf2.compact();
            }

        } finally {
            if (i1 != null) i1.close();
            if (i2 != null) i2.close();
        }
    }

    @Test
    void findLeadMagnetName() {

    }
}