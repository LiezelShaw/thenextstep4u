package com.thenextstep4u.blogging.model;

import com.thenextstep4u.blogging.api.v1.BlogPostVariableDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class BlogPostVariable {
    private String name;
    private String value;

    public BlogPostVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public BlogPostVariableDTO createBlogPostVariableDTO() {
        BlogPostVariableDTO variableDTO = new BlogPostVariableDTO();
        variableDTO.setName(getName());
        variableDTO.setValue(getValue());
        return variableDTO;
    }
}
