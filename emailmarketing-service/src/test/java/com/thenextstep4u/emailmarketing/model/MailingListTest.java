package com.thenextstep4u.emailmarketing.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MailingListTest {
    private MailingList mailingList;

    @BeforeEach
    void setUp() {
        mailingList = new MailingList();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        String mailingListName = "New Mailing List";
        mailingList.setName(mailingListName);
        assertEquals(mailingListName, mailingList.getName());
    }

    @Test
    void getLeadMagnetFilename() {
    }

    @Test
    void getDomainUrl() {
    }
}