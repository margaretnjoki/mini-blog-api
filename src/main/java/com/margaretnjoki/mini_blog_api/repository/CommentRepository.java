package com.margaretnjoki.mini_blog_api.repository;

import com.margaretnjoki.mini_blog_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostId(UUID postId);

    long countByPostId(UUID postId);


}
