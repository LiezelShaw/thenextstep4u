package com.thenextstep4u.emailmarketing.repositories;

import com.thenextstep4u.emailmarketing.model.MailingList;
import com.thenextstep4u.emailmarketing.model.MailingListSubscription;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Map;

public interface MailingListSubscriptionRepository extends CrudRepository<MailingListSubscription, String> {

    MailingListSubscription findByMailingListIdAndEmailSubscriberId(String name, String emailSubscriberId);

    Iterable<? extends MailingListSubscription> findByMailingListIdAndDateTimeSubscribedBefore(String name, LocalDateTime emailDate);

}
