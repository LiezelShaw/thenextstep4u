package com.thenextstep4u.blogging.controller.v1;


import com.thenextstep4u.blogging.api.v1.BlogPostDTO;
import com.thenextstep4u.blogging.api.v1.CommentDTO;
import com.thenextstep4u.blogging.exception.DoesNotExistException;
import com.thenextstep4u.blogging.services.BloggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("blog/api/v1")
public class BlogController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private AuditService auditService;
    private BloggingService bloggingService;

    @Autowired
    public BlogController(BloggingService bloggingService) {
        this.bloggingService = bloggingService;
    }

    @GetMapping(value = "last5")
    public List<BlogPostDTO> findLast5Blogs() {
        logger.info("Find last 5 video blogs");
        List<BlogPostDTO> blogPostDTOs = bloggingService.findLast5Blogs();
        return blogPostDTOs;
    }

    @GetMapping(value = "all")
    public List<BlogPostDTO> findAllBlogs() {
        logger.info("Find all video blogs");
        List<BlogPostDTO> blogPostDTOs = bloggingService.findAllBlogs();
        return blogPostDTOs;
    }

    @GetMapping(value = "{blogId}")
    public BlogPostDTO findBlog(@PathVariable String blogId) throws DoesNotExistException {
        logger.info("Find video blog");
        BlogPostDTO blogPostDTO = bloggingService.findBlog(blogId);
        return blogPostDTO;
    }

    @GetMapping(value = "path/{pathName}")
    public BlogPostDTO findBlogByPathName(@PathVariable String pathName) throws DoesNotExistException {
        logger.info("Find video blog");
        BlogPostDTO blogPostDTO = bloggingService.findBlogByPathName(pathName);
        return blogPostDTO;
    }

    @PostMapping()
    public void postBlog(@RequestBody BlogPostDTO blogPostDTO) {
        logger.info("Create new blog post");
        bloggingService.createNewBlogPost(blogPostDTO);
    }

    @PostMapping(value = "comment/{blogId}")
    public BlogPostDTO postComment(@RequestBody CommentDTO newComment, @PathVariable String blogId) {
        logger.info("Create new comment for blog post");
        return bloggingService.saveCommentForBlog(blogId, newComment);
    }

    @PostMapping(value = "reply/{blogId}")
    public BlogPostDTO postReply(@RequestBody CommentDTO newReply, @PathVariable String blogId) {
        logger.info("Create new reply for comment on post");
        return bloggingService.saveReply(blogId, newReply);
    }
}
