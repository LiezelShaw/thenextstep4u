package com.thenextstep4u.emailmarketing.repositories;

import com.thenextstep4u.emailmarketing.model.Email;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmailRepository extends CrudRepository<Email, String> {

    List<Email> findByMailingListId(String mailingListId);

    Email findByEmailTypeAndMailingListIdAndEmailName(String emailType, String mailingListId, String emailName);

    List<Email> findByEmailTypeAndMailingListIdAndEmailType(String mailingListId, String emailType);
}
