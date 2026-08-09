package com.margaretnjoki.mini_blog_api.controller;

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
    public List<Comment> getCommentsByPost(@PathVariable UUID postId) {
        return commentService.findByPost(postId);
    }

    @PostMapping("/post/{postId}")
    public Comment createComment(@PathVariable UUID postId, @RequestBody String body) {
        return commentService.create(postId, body);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.delete(commentId);
    }
}
