package com.thenextstep4u.web.controller;

import com.thenextstep4u.web.api.v1.blog.BlogPostDTO;
import com.thenextstep4u.web.api.v1.email.LeadMagnetEmailDTO;
import com.thenextstep4u.web.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class MainController {
    private BlogService blogService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public MainController(BlogService blogService) {
        this.blogService = blogService;
    }


    @GetMapping("/")
    public ModelAndView root(HttpSession session) {
        BlogPostDTO[] blogPostDTOS = blogService.find5LatestBlogPosts();
        logger.info("Size:  " + blogPostDTOS.length);
        Arrays.stream(blogPostDTOS).forEach(blogPostDTO -> logger.info(blogPostDTO.getCoverImageURL()));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
        modelAndView.addObject("blogPosts", blogPostDTOS);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
        modelAndView.setViewName("about");
        return modelAndView;
    }



}
