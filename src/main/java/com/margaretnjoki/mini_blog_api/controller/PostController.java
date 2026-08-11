package com.margaretnjoki.mini_blog_api.controller;

import com.margaretnjoki.mini_blog_api.dtos.CreatePostRequest;
import com.margaretnjoki.mini_blog_api.dtos.PagedResponse;
import com.margaretnjoki.mini_blog_api.dtos.PostResponse;
import com.margaretnjoki.mini_blog_api.dtos.UpdatePostRequest;
import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.repository.CommentRepository;
import com.margaretnjoki.mini_blog_api.repository.PostRepository;
import com.margaretnjoki.mini_blog_api.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    public PostController(PostService postService, PostRepository postRepository, CommentRepository commentRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping
    public PostResponse createPost(@RequestBody CreatePostRequest post) {
        return PostResponse.from(postService.create(post), 0L); // Note: You might need to update your service create method to return PostResponse or handle count differently
    }

    @GetMapping("/{slug}")
    public PostResponse getPostBySlug(@PathVariable String slug) {
        log.info("GET /posts/{} - fetching post", slug);

        PostResponse response = postService.getPostBySlugWithCommentCount(slug);

        log.info("GET /posts/{} - post returned", slug);

        return response;
    }
    @GetMapping
    public PagedResponse<PostResponse> search(
            @RequestParam(required = false, defaultValue = "") String tag,
            @RequestParam(required = false, defaultValue = "") String q,
            Pageable pageable) {

        Page<Post> posts = postRepository.search(tag, q, pageable);

        return PagedResponse.from(posts, commentRepository);
    }

    @GetMapping("/owned/{id}")
    public PostResponse getOwnedPostById(@PathVariable UUID id) {
        return PostResponse.from(postService.findOwnedById(id), 0L);
    }

    @PutMapping("/{id}")
    public PostResponse updatePost(@PathVariable UUID id, @RequestBody UpdatePostRequest post) {
        log.info("cache evict");
        return PostResponse.from(postService.update(id, post), 0L);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id) {
        log.info("cache evict");
        postService.delete(id);
    }
}