package com.thenextstep4u.emailmarketing.repositories;

import com.thenextstep4u.emailmarketing.model.EmailSubscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Transactional
public interface EmailSubscriberRepository extends CrudRepository<EmailSubscriber, String> {
    EmailSubscriber findByEmail(String email);

}
