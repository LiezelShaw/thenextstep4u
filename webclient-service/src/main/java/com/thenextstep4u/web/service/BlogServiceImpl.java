package com.thenextstep4u.web.service;

import com.thenextstep4u.web.api.v1.blog.BlogPostDTO;
import com.thenextstep4u.web.api.v1.blog.CommentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BlogServiceImpl implements BlogService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${thenextstep4u.blogging.url}")
    private String blogServiceURL;
//    @Value("${thenextstep4u.gateway.url}")
//    private String gatewayUrl;

    @Value("${thenextstep4u.blogging.path.5latest}")
    private String fiveLatestBlogsPath;
    @Value("${thenextstep4u.blogging.path.all}")
    private String allBlogsPath;
    @Value("${thenextstep4u.blogging.path.blogbyid}")
    private String blogByIdPath;
    @Value("${thenextstep4u.blogging.path.blogbypathname}")
    private String blogByPathName;
    @Value("${thenextstep4u.blogging.path.saveComment}")
    private String saveCommentPath;
    @Value("${thenextstep4u.blogging.path.saveReply}")
    private String saveReplyPath;

    private RestTemplate restTemplate;

    public BlogServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BlogPostDTO[] find5LatestBlogPosts() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(blogServiceURL + fiveLatestBlogsPath);
        logger.info("URL called: " + blogServiceURL + fiveLatestBlogsPath);
        return restTemplate.getForEntity(uriBuilder.toUriString(), BlogPostDTO[].class).getBody();
    }

    @Override
    public BlogPostDTO findBlogById(String blogPostId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(blogServiceURL + blogByIdPath + blogPostId);
        return restTemplate.getForEntity(uriBuilder.toUriString(), BlogPostDTO.class).getBody();
    }

    @Override
    public BlogPostDTO saveCommentForBlog(String blogId, CommentDTO newComment) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(blogServiceURL + saveCommentPath + blogId);
        BlogPostDTO blogPostDTO = restTemplate.postForEntity(uriBuilder.toUriString(), newComment ,BlogPostDTO.class).getBody();
        return blogPostDTO;
    }

    @Override
    public BlogPostDTO saveReplyForBlog(String blogId, CommentDTO newComment) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(blogServiceURL + saveReplyPath + blogId);
        return restTemplate.postForEntity(uriBuilder.toUriString(), newComment ,BlogPostDTO.class).getBody();
    }

    @Override
    public BlogPostDTO[] findAllBlogPosts() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(blogServiceURL + allBlogsPath);
        logger.info("URL called: " + blogServiceURL + allBlogsPath);
        return restTemplate.getForEntity(uriBuilder.toUriString(), BlogPostDTO[].class).getBody();
    }

    @Override
    public BlogPostDTO findBlogbyPathName(String blogPathName) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(blogServiceURL + blogByPathName + blogPathName);
        return restTemplate.getForEntity(uriBuilder.toUriString(), BlogPostDTO.class).getBody();
    }
}
