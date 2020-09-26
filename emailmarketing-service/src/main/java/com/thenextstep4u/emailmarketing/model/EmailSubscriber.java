package com.thenextstep4u.emailmarketing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document
@Data
public class EmailSubscriber implements Serializable {
    @Id
    private String id;
    private String clientName;
    private String email;




    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof EmailSubscriber) {
           EmailSubscriber subscriber = (EmailSubscriber) o;
           if (subscriber.getId() == id) {
               isEqual = true;
           }
        } else {
            isEqual = false;
        }
        return isEqual;
    }
}
