package com.thenextstep4u.emailmarketing.util;


public enum EmailSubscriberStatus {
    PENDING("Pending"),
    VERIFIED("Verified"),
    UNSUBSCRIBED("Unsubscribed"),
    INVALID("Invalid");

    private final String subscriberStatus;

    EmailSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public String value() {
        return subscriberStatus;
    }
}