package com.thenextstep4u.emailmarketing.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class EmailVariable {
    private String name;
    private String value;
}
