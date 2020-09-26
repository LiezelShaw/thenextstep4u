package com.thenextstep4u.emailmarketing.controllers.v1;

import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetDTO;
import com.thenextstep4u.emailmarketing.api.v1.LeadMagnetEmailDTO;
import com.thenextstep4u.emailmarketing.api.v1.UnsubscribeDTO;
import com.thenextstep4u.emailmarketing.exceptions.DoesNotExistException;
import com.thenextstep4u.emailmarketing.services.MarketingCommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("email/api/v1")
public class EmailMarketingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MarketingCommunicationService communicationService;

    @Autowired
    public EmailMarketingController(MarketingCommunicationService communicationService) {
        this.communicationService = communicationService;
    }



    /**
     * Receive email address from landing page, save, send email containing lead magnet, forward to thank you page.
     * @param
     * @return
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void joinList(@RequestBody LeadMagnetEmailDTO leadMagnetEmail) throws DoesNotExistException {
        communicationService.saveEmailAndSendWelcomeDoc(leadMagnetEmail);
    }

    /**
     * Download lead magnet to client.
     * @param mailingListSubscriptionId
     * @return
     */
    @GetMapping(value = "leadmagnet/{mailingListSubscriptionId}")
    public LeadMagnetDTO downloadLeadMagnet(@PathVariable String mailingListSubscriptionId) throws DoesNotExistException, IOException {
        logger.info("Download lead magnet  ...");
//        Audit audit = auditService.createNewAudit("Subscriber: " + subscriberId, "", "", "Download lead magnet", leadmagnetname);
        return communicationService.getLeadMagnet(mailingListSubscriptionId);
    }

    @PutMapping(value = "{mailingListSubscriptionId}/{emailId}")
    @ResponseStatus(HttpStatus.OK)
    public void setEmailLinkClickedForSubscriber(@PathVariable String mailingListSubscriptionId, @PathVariable String emailId){
        communicationService.setEmailLinkClickedForSubscriber(mailingListSubscriptionId, emailId);
    }

    @PutMapping(value = "unsubscribe/{mailingListSubscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void unsubscribe(@PathVariable String mailingListSubscriptionId) {
        logger.info("Unsubscribing a subscriber from the list");
        try {
            communicationService.unsubscribe(mailingListSubscriptionId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }


}
