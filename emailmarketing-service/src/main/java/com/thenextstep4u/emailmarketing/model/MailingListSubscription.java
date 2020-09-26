package com.thenextstep4u.emailmarketing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document
public class MailingListSubscription {
    @Id
    private String id;
    private String emailSubscriberId;
    private String mailingListId;
    private LocalDateTime dateTimeSubscribed;;
    private String status;
    private String statusReason;
}
