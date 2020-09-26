package com.thenextstep4u.web.controller;

import com.thenextstep4u.web.api.v1.blog.BlogPostDTO;
import com.thenextstep4u.web.api.v1.email.LeadMagnetDTO;
import com.thenextstep4u.web.api.v1.email.LeadMagnetEmailDTO;
import com.thenextstep4u.web.service.BlogService;
import com.thenextstep4u.web.service.MarketingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("web/email")
public class MarketingController implements WebMvcConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MarketingService marketingService;
    private BlogService blogService;

    public MarketingController(MarketingService marketingService, BlogService blogService) {
        this.marketingService = marketingService;
        this.blogService = blogService;
    }

    /**
     * Receive email address from landing page, save, send email containing lead magnet, forward to thank you page.
     * @param leadMagnetEmailDTO
     * @return
     */
    @RequestMapping(value = "joinlist", method = RequestMethod.POST)
    public ModelAndView joinList(ModelAndView modelAndView, @Valid @ModelAttribute("leadMagnetEmailDTO")  LeadMagnetEmailDTO leadMagnetEmailDTO, BindingResult bindingResult) {
        logger.info("Received lead magnet request ...");
        if (bindingResult.hasErrors()) {
            logger.error("BindingResult has errors!");
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                logger.error(fieldError.toString());
            }
            BlogPostDTO[] blogPostDTOS = blogService.find5LatestBlogPosts();
            modelAndView.addObject("leadMagnetEmailDTO", leadMagnetEmailDTO);
            modelAndView.addObject("blogPosts", blogPostDTOS);
            modelAndView.setViewName("index");
        } else {
            try {
                leadMagnetEmailDTO.setSystemName("TheNextStep4u");
                leadMagnetEmailDTO.setMailingListName("NextStepSubscriptionList");
                marketingService.sendLeadMagnetEmail(leadMagnetEmailDTO);
                modelAndView.setViewName("thankyou");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
//                    modelAndView.setViewName("/error/error");
            }
        }

        return modelAndView;
    }

    /**
     * Download lead magnet to client.
     * @param mailingListSubscriptionId
     * @return
     */
    @GetMapping(value = "leadmagnet/{mailingListSubscriptionId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadLeadMagnet(@PathVariable String mailingListSubscriptionId) throws  IOException {
        logger.info("Download lead magnet  ...");
        LeadMagnetDTO leadMagnetDTO = marketingService.getLeadMagnet(mailingListSubscriptionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + leadMagnetDTO.getLeadmagnetName());
        byte[] file = leadMagnetDTO.getLeadMagnet().get();
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(file);
    }

    @GetMapping(value = "unsubscribe/{mailingListSubscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void unsubscribe(@PathVariable String mailingListSubscriptionId) {
        logger.info("Unsubscribing a subscriber from the list");
        try {
            marketingService.unsubscribe(mailingListSubscriptionId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

}
