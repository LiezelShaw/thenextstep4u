package com.thenextstep4u.blogging.services;

import com.thenextstep4u.blogging.api.v1.BlogPostDTO;
import com.thenextstep4u.blogging.api.v1.CommentDTO;
import com.thenextstep4u.blogging.model.BlogPost;
import com.thenextstep4u.blogging.model.BlogPostVariable;
import com.thenextstep4u.blogging.model.Comment;
import com.thenextstep4u.blogging.repositories.BlogPostRepository;
import com.thenextstep4u.blogging.repositories.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BloggingServiceImpl implements BloggingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BlogPostRepository blogPostRepository;
    private CommentRepository commentRepository;

    public BloggingServiceImpl(BlogPostRepository blogPostRepository, CommentRepository commentRepository) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public BlogPostDTO findBlog(String id) {
        BlogPost blogPost = blogPostRepository.findById(id).get();
        return createBlogPostDTOWithComments(blogPost);
    }

    @Override
    public BlogPostDTO saveCommentForBlog(String blogId, CommentDTO newComment) {
        BlogPost blogPost = blogPostRepository.findById(blogId).get();
        Comment comment = new Comment();
        comment.setParentType(Comment.PARENT_TYPE_BLOGPOST);
        comment.setParentId(blogId);
        comment.setDateTimePosted(LocalDateTime.now());
        comment.setDetails(newComment.getDetails());
        comment.setFromEmail(newComment.getFromEmail());
        comment.setFromName(newComment.getFromName());
        commentRepository.save(comment);
        return createBlogPostDTOWithComments(blogPost);
    }

    private BlogPostDTO createBlogPostDTOWithComments(BlogPost blogPost) {
        BlogPostDTO blogPostDTO = blogPost.createBlogPostDTO();
        Set<Comment> commentSet = commentRepository.findByParentTypeAndParentId(Comment.PARENT_TYPE_BLOGPOST, blogPost.getId());
        commentSet.forEach(comment -> {
            blogPostDTO.getComments().add(getCommentsToComment(comment));
        });
        return blogPostDTO;
    }

    private CommentDTO getCommentsToComment(Comment comment) {
        CommentDTO commentDTO = comment.createCommentDTO();
        Set<Comment> replies = commentRepository.findByParentTypeAndParentId(Comment.PARENT_TYPE_COMMENT, comment.getId());
        replies.forEach(reply -> {
            commentDTO.getReplies().add(getCommentsToComment(reply));
        });
        return commentDTO;
    }

    @Override
    public BlogPostDTO saveReply(String blogId, CommentDTO newComment) {
        logger.info("Blog Id in BloggingServiceImpl.saveReply: " + blogId);
        logger.info("newComment parentId in BloggingServiceImpl.saveReply: " + newComment.getParentId());
        Comment comment = new Comment();
        comment.setParentType(Comment.PARENT_TYPE_COMMENT);
        comment.setParentId(newComment.getParentId());
        comment.setDateTimePosted(LocalDateTime.now());
        comment.setDetails(newComment.getDetails());
        comment.setFromEmail(newComment.getFromEmail());
        comment.setFromName(newComment.getFromName());
        commentRepository.save(comment);
        return createBlogPostDTOWithComments(blogPostRepository.findById(blogId).get());
    }

    @Override
    public List<BlogPostDTO> findLast5Blogs() {
        ArrayList<BlogPostDTO> blogPostDTOs = new ArrayList<>();
        blogPostRepository.findTop5ByOrderByDatePostedDesc().forEach(blogPost -> {
            blogPostDTOs.add(createBlogPostDTOWithComments(blogPost));
        });
        return blogPostDTOs;
    }

    @Override
    public List<BlogPostDTO> findAllBlogs() {
        ArrayList<BlogPostDTO> blogPostDTOs = new ArrayList<>();
        blogPostRepository.findAllByOrderByDatePostedDesc().forEach(blogPost -> {
            blogPostDTOs.add(createBlogPostDTOWithComments(blogPost));
        });
        return blogPostDTOs;
    }

    @Override
    @Transactional
    public void createNewBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = new BlogPost();
        blogPost.setDatePosted(LocalDate.now());
        blogPost.setHtmlTemplate(blogPostDTO.getHtmlTemplate());
        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setCoverImageURL(blogPostDTO.getCoverImageURL());
        blogPost.setFirstParagraph(blogPostDTO.getFirstParagraph());
        blogPostDTO.getVariables().forEach(blogPostVariableDTO -> {
            BlogPostVariable variable = new BlogPostVariable(blogPostVariableDTO.getName(),blogPostVariableDTO.getValue());
            blogPost.getBlogPostVariables().add(variable);
        });
        blogPostRepository.save(blogPost);
    }

    @Override
    public BlogPostDTO findBlogByPathName(String pathName) {
        BlogPost blogPost = blogPostRepository.findByUrlPathName(pathName);
        return createBlogPostDTOWithComments(blogPost);
    }

}
