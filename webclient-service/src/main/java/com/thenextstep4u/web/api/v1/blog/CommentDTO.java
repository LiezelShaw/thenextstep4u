package com.thenextstep4u.web.api.v1.blog;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentDTO implements Serializable {
    private String id;
    private String parentId;
    private String details;
    private String fromEmail;
    private String fromName;
    private String dateTimePosted;

    private String spamAnswer;

    private List<CommentDTO> replies = new ArrayList<>();
}
