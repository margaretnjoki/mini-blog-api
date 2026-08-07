package com.margaretnjoki.mini_blog_api.repository;

import com.margaretnjoki.mini_blog_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findBySlug(String slug);

}
