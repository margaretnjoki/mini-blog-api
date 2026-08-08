package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.entity.Comment;
import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.exception.ResourceNotFoundException;
import com.margaretnjoki.mini_blog_api.repository.CommentRepository;
import com.margaretnjoki.mini_blog_api.security.CurrentUserProvider;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CurrentUserProvider currentUserProvider;

    public CommentService(CommentRepository commentRepository, PostService postService, CurrentUserProvider currentUserProvider) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.currentUserProvider = currentUserProvider;
    }

    public List<Comment> findByPost(UUID postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment create(UUID postId, String body) {
        Post post = postService.findById(postId);   // however you resolve the post
        Comment comment = Comment.builder()
                .post(post)
                .author(currentUserProvider.getCurrentUser())
                .body(body)
                .createdAt(Instant.now())
                .build();
        return commentRepository.save(comment);
    }

    public void delete(UUID commentId) {
        UUID userId = currentUserProvider.getCurrentUser().getId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ResourceNotFoundException("Comment", commentId);
        }
        commentRepository.delete(comment);
    }
}

