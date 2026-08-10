package com.margaretnjoki.mini_blog_api.dtos;

import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.repository.CommentRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static PagedResponse<PostResponse> from(Page<Post> page, CommentRepository commentRepository) {
        List<PostResponse> content = page.getContent().stream()
                .map(post -> {
                    long commentCount = commentRepository.countByPostId(post.getId());
                    return PostResponse.from(post, commentCount);
                })
                .toList();

        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}