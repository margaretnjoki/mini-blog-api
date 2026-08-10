package com.margaretnjoki.mini_blog_api.controller;

import com.margaretnjoki.mini_blog_api.dtos.CommentResponse;
import com.margaretnjoki.mini_blog_api.dtos.CreateCommentRequest;
import com.margaretnjoki.mini_blog_api.entity.Comment;
import com.margaretnjoki.mini_blog_api.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    public List<CommentResponse> getCommentsByPost(@PathVariable UUID postId) {
        return commentService.findByPost(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @PostMapping("/post/{postId}")
    public CommentResponse createComment(@PathVariable UUID postId, @RequestBody CreateCommentRequest req) {
        return CommentResponse.from(commentService.create(postId, req.body()));
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.delete(commentId);
    }
}
