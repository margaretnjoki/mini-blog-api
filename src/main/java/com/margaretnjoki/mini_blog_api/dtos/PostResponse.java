package com.margaretnjoki.mini_blog_api.dtos;

import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.entity.Tag;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record PostResponse(
        UUID id,
        String title,
        String slug,
        String bodyMd,
        String authorName,
        Set<String> tags,
        long commentCount,
        Instant publishedAt,
        Instant createdAt
) {
    public static PostResponse from(Post p, long commentCount) {
        Set<String> tagNames = p.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        return new PostResponse(
                p.getId(),
                p.getTitle(),
                p.getSlug(),
                p.getBodyMd(),
                p.getAuthor().getDisplayName(),
                tagNames,
                commentCount,
                p.getPublishedAt(),
                p.getCreatedAt()
        );
    }
}