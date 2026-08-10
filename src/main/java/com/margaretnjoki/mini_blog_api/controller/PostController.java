package com.margaretnjoki.mini_blog_api.controller;


import com.margaretnjoki.mini_blog_api.dtos.CreatePostRequest;
import com.margaretnjoki.mini_blog_api.dtos.PostResponse;
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
    public PostResponse createPost(@RequestBody CreatePostRequest post) {
        return PostResponse.from(postService.create(post));
    }

    @GetMapping("/{slug}")
    public PostResponse getPostBySlug(@PathVariable String slug) {
        return PostResponse.from(postService.findBySlug(slug));
    }

    @GetMapping("/owned/{id}")
    public PostResponse getOwnedPostById(@PathVariable UUID id) {
        return PostResponse.from(postService.findOwnedById(id));
    }

    @PutMapping("/{id}")
    public PostResponse updatePost(@PathVariable UUID id, @RequestBody UpdatePostRequest post) {
        return PostResponse.from(postService.update(id, post));
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id) {
        postService.delete(id);
    }
}
