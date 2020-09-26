package com.thenextstep4u.blogging.api.v1;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogPostDTO implements Serializable {
    private String id;
    private String htmlTemplate;
    private String title;
    private String datePosted;
    private String coverImageURL;
    private String firstParagraph;
    private String urlPathName;
    private List<BlogPostVariableDTO> variables = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();
}
