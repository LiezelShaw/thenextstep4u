package com.thenextstep4u.emailmarketing.util;

public enum EmailType {
    LEADMAGNET("Lead Magnet Welcome"),
    AUTORESPONDER("Auto Responder"),
    SUBSCRIPTION("Subscription");

    private final String emailType;

    EmailType(String emailType) {
        this.emailType = emailType;
    }

    public String value() {
        return emailType;
    }
}
