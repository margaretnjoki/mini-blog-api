package com.margaretnjoki.mini_blog_api.controller;


import com.margaretnjoki.mini_blog_api.dtos.CreatePostRequest;
import com.margaretnjoki.mini_blog_api.dtos.UpdatePostRequest;
import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest post) {
        return postService.create(post);
    }

    @GetMapping("/{slug}")
    public Post getPostBySlug(@PathVariable String slug) {
        return postService.findBySlug(slug);
    }

    @GetMapping("/owned/{id}")
    public Post getOwnedPostById(@PathVariable UUID id) {
        return postService.findOwnedById(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable UUID id, @RequestBody UpdatePostRequest post) {
        return postService.update(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id) {
        postService.delete(id);
    }
}
