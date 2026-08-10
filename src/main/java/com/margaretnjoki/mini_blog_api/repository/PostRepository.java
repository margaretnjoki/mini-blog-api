package com.margaretnjoki.mini_blog_api.repository;

import com.margaretnjoki.mini_blog_api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findBySlug(String slug);

    @Query("""
    SELECT DISTINCT p FROM Post p
    LEFT JOIN p.tags t
    WHERE (:tag = '' OR LOWER(t.name) = LOWER(:tag))
      AND (:q = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')))
      AND p.publishedAt IS NOT NULL
""")
    Page<Post> search(@Param("tag") String tag, @Param("q") String q, Pageable pageable);
    }
