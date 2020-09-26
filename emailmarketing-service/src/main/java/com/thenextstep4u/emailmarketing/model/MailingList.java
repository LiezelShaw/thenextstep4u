package com.thenextstep4u.emailmarketing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
@Data
public class MailingList {
    @Id
    private String id;
    private String name;
    private String systemName;
    private String leadMagnetFilename;
    private String domainUrl;
    private String logoURL;
}
