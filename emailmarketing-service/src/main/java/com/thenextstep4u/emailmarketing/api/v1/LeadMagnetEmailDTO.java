package com.thenextstep4u.emailmarketing.api.v1;

import lombok.Data;


import java.io.Serializable;

@Data
public class LeadMagnetEmailDTO implements Serializable {
    private String email;
    private String clientName;
    private String systemName;
    private String mailingListName;
}
