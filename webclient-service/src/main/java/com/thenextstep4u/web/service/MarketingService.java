package com.thenextstep4u.web.service;

import com.thenextstep4u.web.api.v1.email.LeadMagnetDTO;
import com.thenextstep4u.web.api.v1.email.LeadMagnetEmailDTO;

public interface MarketingService {
    void sendLeadMagnetEmail(LeadMagnetEmailDTO leadMagnetEmailDTO);

    void setEmailLinkClicked(String mailSubscriptionId, String emailId);

    LeadMagnetDTO getLeadMagnet(String mailingListSubscriptionId);

    void unsubscribe(String mailingListSubscriptionId);
}
