package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.dtos.CreatePostRequest;
import com.margaretnjoki.mini_blog_api.dtos.PostResponse;
import com.margaretnjoki.mini_blog_api.dtos.UpdatePostRequest;
import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.entity.Tag;
import com.margaretnjoki.mini_blog_api.entity.User;
import com.margaretnjoki.mini_blog_api.exception.ResourceNotFoundException;
import com.margaretnjoki.mini_blog_api.repository.CommentRepository;
import com.margaretnjoki.mini_blog_api.repository.PostRepository;
import com.margaretnjoki.mini_blog_api.repository.TagRepository;
import com.margaretnjoki.mini_blog_api.security.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private CurrentUserProvider currentUserProvider;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository, CurrentUserProvider currentUserProvider, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.currentUserProvider = currentUserProvider;
        this.commentRepository = commentRepository;
    }

    public Post create(CreatePostRequest req) {
        User author = currentUserProvider.getCurrentUser();
        String slug = uniqueSlug(generateSlug(req.title()));

        Post post = Post.builder()
                .author(author)
                .title(req.title())
                .slug(slug)
                .bodyMd(req.bodyMd())
                .createdAt(Instant.now())
                .tags(resolveTags(req.tagNames()))
                .build();

        post.setPublishedAt(java.time.Instant.now());
        return postRepository.save(post);
    }

    public Post findBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post", slug));
    }

    public PostResponse getPostBySlugWithCommentCount(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.from(post, commentCount);
    }

    public Post findOwnedById(UUID id) {
        UUID userId = currentUserProvider.getCurrentUser().getId();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new ResourceNotFoundException("Post", id);
        }
        return post;
    }

    public Post update(UUID id, UpdatePostRequest req) {
        Post post = findOwnedById(id);
        if (req.title() != null) post.setTitle(req.title());
        if (req.bodyMd() != null) post.setBodyMd(req.bodyMd());
        if (req.tagNames() != null) post.setTags(resolveTags(req.tagNames()));
        if (Boolean.TRUE.equals(req.published()) && post.getPublishedAt() == null) {
            post.setPublishedAt(Instant.now());
        }
        return postRepository.save(post);
    }

    public void delete(UUID id) {
        postRepository.delete(findOwnedById(id));
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }

    private String uniqueSlug(String base) {
        String slug = base;
        int counter = 1;
        while (postRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    private Set<Tag> resolveTags(Set<String> tagNames) {
        if (tagNames == null) return new HashSet<>();
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(
                                Tag.builder().name(name).build())))
                .collect(Collectors.toSet());
    }

    public Post findById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));

    }
    public PostResponse getPostWithComments(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        long commentCount = commentRepository.countByPostId(post.getId());

        return PostResponse.from(post, commentCount);
    }
}
