package com.thenextstep4u.emailmarketing.repositories;

import com.thenextstep4u.emailmarketing.model.Email;
import com.thenextstep4u.emailmarketing.model.MailingListSubscription;
import com.thenextstep4u.emailmarketing.model.SentEmailInfo;
import org.springframework.data.repository.CrudRepository;

public interface SentEmailInfoRepository extends CrudRepository<SentEmailInfo, String> {
    SentEmailInfo findByEmailIdAndMailingListSubscriptionId(String emailId, String mailingListSubscriptionId);
}
