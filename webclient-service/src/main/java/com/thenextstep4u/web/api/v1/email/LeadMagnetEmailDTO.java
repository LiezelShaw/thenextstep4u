package com.thenextstep4u.web.api.v1.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class LeadMagnetEmailDTO implements Serializable {
    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "The email address is not valid")
    @NotNull(message = "Email address cannot be empty")
    private String email;

    @NotEmpty(message = "Please specify your name")
    @NotNull(message = "Please specify your name")
    private String clientName;

    private String systemName;
    private String mailingListName;
}
