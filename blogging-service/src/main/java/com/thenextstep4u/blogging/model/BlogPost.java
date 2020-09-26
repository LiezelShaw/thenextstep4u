package com.thenextstep4u.blogging.model;

import com.thenextstep4u.blogging.api.v1.BlogPostDTO;
import com.thenextstep4u.blogging.api.v1.BlogPostVariableDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.swing.text.DateFormatter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
public class BlogPost implements Serializable {
    @Id
    private String id;
    private String title;
    private String htmlTemplate;
    private LocalDate datePosted;
    private String coverImageURL;
    private String firstParagraph;
    private String urlPathName;

    private Set<BlogPostVariable> blogPostVariables = new HashSet<>();

//    private Set<Comment> comments = new HashSet<>();

    public BlogPostDTO createBlogPostDTO() {
        BlogPostDTO blogPostDTO = new BlogPostDTO();
        blogPostDTO.setId(id);
        blogPostDTO.setHtmlTemplate(htmlTemplate);
        blogPostDTO.setTitle(title);
        blogPostDTO.setDatePosted(LocalDate.now().toString());
        blogPostDTO.setCoverImageURL(coverImageURL);
        blogPostDTO.setFirstParagraph(firstParagraph);
        blogPostDTO.setUrlPathName(urlPathName);
        getBlogPostVariables().forEach(variable -> {
            blogPostDTO.getVariables().add(variable.createBlogPostVariableDTO());
        });
        return blogPostDTO;
    }


}
