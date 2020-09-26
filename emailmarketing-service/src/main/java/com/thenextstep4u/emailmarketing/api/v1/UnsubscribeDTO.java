package com.thenextstep4u.emailmarketing.api.v1;

import lombok.Data;

import java.io.Serializable;

@Data
public class UnsubscribeDTO implements Serializable {
    private String subscriptionId;
    private String reason;
}
