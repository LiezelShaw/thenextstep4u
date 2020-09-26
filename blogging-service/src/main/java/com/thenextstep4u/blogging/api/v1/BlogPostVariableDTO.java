package com.thenextstep4u.blogging.api.v1;

import com.thenextstep4u.blogging.model.BlogPostVariable;
import lombok.Data;

@Data
public class BlogPostVariableDTO {
    String name;
    String value;
}
