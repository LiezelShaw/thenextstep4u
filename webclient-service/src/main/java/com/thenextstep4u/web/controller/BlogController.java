package com.thenextstep4u.web.controller;

import com.thenextstep4u.web.api.v1.blog.BlogPostDTO;
import com.thenextstep4u.web.api.v1.blog.CommentDTO;
import com.thenextstep4u.web.api.v1.email.LeadMagnetEmailDTO;
import com.thenextstep4u.web.service.BlogService;
import com.thenextstep4u.web.service.MarketingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("web/blog")
public class BlogController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BlogService blogService;
    private MarketingService marketingService;

    public BlogController(BlogService blogService, MarketingService marketingService) {
        this.blogService = blogService;
        this.marketingService = marketingService;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ModelAndView root(HttpSession session) {
        BlogPostDTO[] blogPostDTOS = blogService.findAllBlogPosts();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("blogPosts", blogPostDTOS);
        modelAndView.setViewName("blog/blogMain");
        return modelAndView;
    }

    @RequestMapping(value = "{blogPathName}", method = RequestMethod.GET)
    public ModelAndView displayBlog(@PathVariable String blogPathName) {
        logger.info("Display video blog");
        ModelAndView modelAndView = new ModelAndView();
        try {
            BlogPostDTO blogPostDTO = blogService.findBlogbyPathName(blogPathName);
            modelAndView.addObject("blogPostDTO", blogPostDTO);
            modelAndView.addObject("newComment", new CommentDTO());
            modelAndView.addObject("newReply", new CommentDTO());
            modelAndView.addObject("replyFormExpanded", 0);
            modelAndView.addObject("isCommentFormExpanded", false);
            modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
            modelAndView.setViewName(blogPostDTO.getHtmlTemplate());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "{blogPathName}/{emailId}/{mailSubscriptionId}", method = RequestMethod.GET)
    public ModelAndView displayBlogForEmailSubscriber(@PathVariable String blogPathName, @PathVariable String mailSubscriptionId,
                                                      @PathVariable String emailId) {
        logger.info("Display video blog");
        ModelAndView modelAndView = new ModelAndView();
        try {
            BlogPostDTO blogPostDTO = blogService.findBlogbyPathName(blogPathName);
            marketingService.setEmailLinkClicked(mailSubscriptionId,emailId);
            modelAndView.addObject("blogPostDTO", blogPostDTO);
            modelAndView.addObject("newComment", new CommentDTO());
            modelAndView.addObject("newReply", new CommentDTO());
            modelAndView.addObject("replyFormExpanded", 0);
            modelAndView.addObject("isCommentFormExpanded", false);
            modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
            modelAndView.setViewName(blogPostDTO.getHtmlTemplate());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "comment/{blogId}", method = RequestMethod.POST)
    public ModelAndView postComment(ModelAndView modelAndView, @ModelAttribute("newComment") @Valid CommentDTO newComment, BindingResult bindingResult, @PathVariable String blogId) {
        logger.info("Create new comment for blog post");
        try {
            if (bindingResult.hasErrors() ||
                    (!newComment.getSpamAnswer().equalsIgnoreCase("five") &&
                            !newComment.getSpamAnswer().equals("5"))) {
                if ((!newComment.getSpamAnswer().equalsIgnoreCase("five") &&
                        !newComment.getSpamAnswer().equals("5"))) {
                    logger.info("SPAMMER ALERT !!! - Spam comment message picked up.");
                    bindingResult.rejectValue("spamAnswer", "newComment.spamAnswer", "The answer to the question is incorrect!.");
                }
                modelAndView.addObject("newComment", newComment);
                modelAndView.addObject("newReply", new CommentDTO());
                BlogPostDTO blogPostDTO = blogService.findBlogById(blogId);
                modelAndView.addObject("blogPostDTO", blogPostDTO);
                modelAndView.addObject("replyFormExpanded", 0);
                modelAndView.addObject("isCommentFormExpanded", true);
                modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
                modelAndView.setViewName(blogPostDTO.getHtmlTemplate());
                return modelAndView;
            }
            BlogPostDTO blogPostDTO = blogService.saveCommentForBlog(blogId, newComment);
            modelAndView.addObject("blogPostDTO", blogPostDTO);
            modelAndView.addObject("newComment", new CommentDTO());
            modelAndView.addObject("newReply", new CommentDTO());
            modelAndView.addObject("replyFormExpanded", 0);
            modelAndView.addObject("isCommentFormExpanded", false);
            modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
            modelAndView.setViewName(blogPostDTO.getHtmlTemplate());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "reply/{blogId}", method = RequestMethod.POST)
    public ModelAndView postReply(ModelAndView modelAndView, @ModelAttribute("newReply") @Valid CommentDTO newReply,
                                  BindingResult bindingResult, @PathVariable String blogId) {
        logger.info("Create new reply for comment on post");
        try {
            if (bindingResult.hasErrors() ||
                    (!newReply.getSpamAnswer().equalsIgnoreCase("five") &&
                            !newReply.getSpamAnswer().equals("5"))) {
                if ((!newReply.getSpamAnswer().equalsIgnoreCase("five") &&
                        !newReply.getSpamAnswer().equals("5"))) {
                    logger.info("SPAMMER ALERT !!! - Spam comment message picked up.");
                    bindingResult.rejectValue("spamAnswer", "newComment.spamAnswer", "The answer to the question is incorrect!.");
                }
                modelAndView.addObject("newReply", newReply);
                modelAndView.addObject("newComment", new CommentDTO());
                BlogPostDTO blogPostDTO = blogService.findBlogById(blogId);
                modelAndView.addObject("blogPostDTO", blogPostDTO);
                modelAndView.addObject("replyFormExpanded", newReply.getParentId());
                logger.debug("replyFormExpanded:  " + newReply.getParentId());
                modelAndView.addObject("isCommentFormExpanded", false);
                modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
                modelAndView.setViewName(blogPostDTO.getHtmlTemplate());
                return modelAndView;
            }
            BlogPostDTO blogPostDTO = blogService.saveReplyForBlog(blogId, newReply);
            modelAndView.addObject("blogPostDTO", blogPostDTO);
            modelAndView.addObject("newComment", new CommentDTO());
            modelAndView.addObject("newReply", new CommentDTO());
            modelAndView.addObject("replyFormExpanded", 0);
            modelAndView.addObject("isCommentFormExpanded", false);
            modelAndView.addObject("leadMagnetEmailDTO", new LeadMagnetEmailDTO());
            modelAndView.setViewName(blogPostDTO.getHtmlTemplate());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return modelAndView;
    }

}
