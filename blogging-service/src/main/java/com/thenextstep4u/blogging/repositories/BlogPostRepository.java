package com.thenextstep4u.blogging.repositories;

import com.thenextstep4u.blogging.model.BlogPost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface BlogPostRepository  extends CrudRepository<BlogPost, String> {
    BlogPost findByTitle(String vlogName);

    Iterable<BlogPost> findTop5ByOrderByDatePostedDesc();

    Iterable<BlogPost> findAllByOrderByDatePostedDesc();

    BlogPost findByUrlPathName(String pathName);
}
