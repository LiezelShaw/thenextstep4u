package com.thenextstep4u.web.service;

import com.thenextstep4u.web.api.v1.email.LeadMagnetDTO;
import com.thenextstep4u.web.api.v1.email.LeadMagnetEmailDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class MarketingServiceImpl implements MarketingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${thenextstep4u.emailmarketing.url}")
    private String emailMarketingURL;
//    @Value("${thenextstep4u.gateway.url}")
//    private String gatewayUrl;
    @Value("${thenextstep4u.emailmarketing.path.sendleadmagnet}")
    private String sendLeadMagnetPath;
    @Value("${thenextstep4u.emailmarketing.path.emaillinkclicked}")
    private String emailLinkClickedPath;
    @Value("${thenextstep4u.emailmarketing.path.getleadmagnet}")
    private String getLeadMagnetPath;
    @Value("${thenextstep4u.emailmarketing.path.unsubscribe}")
    private String unsubscribePath;

    private RestTemplate restTemplate;

    public MarketingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendLeadMagnetEmail(LeadMagnetEmailDTO leadMagnetEmailDTO) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(emailMarketingURL + sendLeadMagnetPath);
        restTemplate.put(uriBuilder.toUriString(), leadMagnetEmailDTO);
    }

    @Override
    public void setEmailLinkClicked(String mailSubscriptionId, String emailId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(emailMarketingURL + emailLinkClickedPath + mailSubscriptionId + "/"
                + emailId);
        restTemplate.put(uriBuilder.toUriString(), null);
    }

    @Override
    public LeadMagnetDTO getLeadMagnet(String mailingListSubscriptionId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(emailMarketingURL + getLeadMagnetPath + mailingListSubscriptionId);
        LeadMagnetDTO leadMagnetDTO = restTemplate.getForObject(uriBuilder.toUriString(), LeadMagnetDTO.class);
        byte[] utf8Bytes = StringUtils.getBytesUtf8(leadMagnetDTO.getLeadmagnetFile());
        leadMagnetDTO.setLeadMagnet(Optional.of(Base64.decodeBase64(utf8Bytes)));
        return leadMagnetDTO;
    }

    @Override
    public void unsubscribe(String mailingListSubscriptionId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(emailMarketingURL + unsubscribePath + mailingListSubscriptionId);
        restTemplate.put(uriBuilder.toUriString(), null);
    }


}
