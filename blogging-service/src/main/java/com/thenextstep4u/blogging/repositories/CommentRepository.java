package com.thenextstep4u.blogging.repositories;

import com.thenextstep4u.blogging.model.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CommentRepository extends CrudRepository<Comment, String> {
    Set<Comment> findByParentTypeAndParentId(String parentType, String parentId);
}
