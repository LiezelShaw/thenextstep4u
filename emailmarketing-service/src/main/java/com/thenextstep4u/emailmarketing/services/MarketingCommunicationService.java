package com.thenextstep4u.emailmarketing.services;

import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetDTO;
import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetEmailDTO;
import com.thenextstep4u.emailmarketing.api.v1.UnsubscribeDTO;
import com.thenextstep4u.emailmarketing.exceptions.DoesNotExistException;

import java.io.InputStream;


public interface MarketingCommunicationService {
    void saveEmailAndSendWelcomeDoc(LeadMagnetEmailDTO leadMagnetEmail) throws DoesNotExistException;

    void sendAutoResponderEmails();

    void  sendWeeklyBlogEmails();

    LeadMagnetDTO getLeadMagnet(String mailingListSubscriptionId) throws DoesNotExistException;

    String findLeadMagnetName(String mailingListSubscriptionId);

//    void sendListSubscriberEmails();
//
    void unsubscribe(String mailingListSubscriptionId);

    void setEmailLinkClickedForSubscriber(String mailingListSubscriptionId, String emailId);


}
