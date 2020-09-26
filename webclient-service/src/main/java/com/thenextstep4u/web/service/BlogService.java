package com.thenextstep4u.web.service;

import com.thenextstep4u.web.api.v1.blog.BlogPostDTO;
import com.thenextstep4u.web.api.v1.blog.CommentDTO;

public interface BlogService {
    BlogPostDTO[] find5LatestBlogPosts();

    BlogPostDTO findBlogById(String blogPostId);

    BlogPostDTO saveCommentForBlog(String blogId, CommentDTO newComment);

    BlogPostDTO saveReplyForBlog(String blogId, CommentDTO newComment);

    BlogPostDTO[] findAllBlogPosts();

    BlogPostDTO findBlogbyPathName(String blogPathName);
}
