package com.thenextstep4u.emailmarketing.repositories;

import com.thenextstep4u.emailmarketing.model.MailingList;
import org.springframework.data.repository.CrudRepository;

public interface MailingListRepository extends CrudRepository<MailingList, String> {
    MailingList findByName(String mailingListName);
}
