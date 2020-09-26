package com.thenextstep4u.web.api.v1.blog;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogPostDTO implements Serializable {
    private String id;
    private String htmlTemplate;
    private String title;
    private LocalDate datePosted;
    private String coverImageURL;
    private String firstParagraph;
    private String urlPathName;
    private List<BlogPostVariableDTO> variables = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();
}
