package com.thenextstep4u.blogging.services;

import com.thenextstep4u.blogging.api.v1.BlogPostDTO;
import com.thenextstep4u.blogging.api.v1.CommentDTO;
import com.thenextstep4u.blogging.exception.DoesNotExistException;

import java.util.List;

public interface BloggingService {

    BlogPostDTO findBlog(String id) throws DoesNotExistException;

    BlogPostDTO saveCommentForBlog(String blogId, CommentDTO newComment);

    BlogPostDTO saveReply(String blogId, CommentDTO newComment);

    List<BlogPostDTO> findLast5Blogs();

    List<BlogPostDTO> findAllBlogs();

    void createNewBlogPost(BlogPostDTO blogPostDTO);

    BlogPostDTO findBlogByPathName(String pathName);
}
