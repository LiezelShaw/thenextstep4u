package com.thenextstep4u.blogging.model;


import com.thenextstep4u.blogging.api.v1.CommentDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
public class Comment implements Serializable {
    public static final String PARENT_TYPE_COMMENT = "Comment";
    public static final String PARENT_TYPE_BLOGPOST = "BlogPost";

    @Id
    private String id;
    private String parentId;
    private String parentType;
    private String details;
    private String fromName;
    private String fromEmail;
    private LocalDateTime dateTimePosted;

    public CommentDTO createCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setParentId(parentId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        commentDTO.setDateTimePosted(formatter.format(dateTimePosted));
        commentDTO.setDetails(details);
        commentDTO.setFromEmail(fromEmail);
        commentDTO.setFromName(fromName);
        return commentDTO;
    }
}
