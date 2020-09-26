package com.thenextstep4u.emailmarketing.model;

import com.thenextstep4u.emailmarketing.util.EmailType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document
@Data
public class Email implements Serializable {
    @Id
    private String id;
    private String emailName;
    private String emailType; //Lead Magnet, Auto Responder, Weekly subscription
    private int unitNr;
    private String unitType;
    private String templateName;
    private String mailingListId;
    private String subject;
    private boolean isActive;
    private String fromAddress;
    private String replyToAddress;
    private Set<EmailVariable> emailVariables = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if((o != null) && (o instanceof Email)) {
            Email subscriber = (Email) o;
            if (subscriber.getId() == id) {
                isEqual = true;
            }
        } else {
            isEqual = false;
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, emailName);
    }
}
