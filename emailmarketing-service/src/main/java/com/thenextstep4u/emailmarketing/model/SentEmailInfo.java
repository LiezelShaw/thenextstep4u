package com.thenextstep4u.emailmarketing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class SentEmailInfo {
    @Id
    private String id;
    private String emailId;
    private String mailingListSubscriptionId;
    private boolean linkClicked;
    private boolean emailOpened;
}
