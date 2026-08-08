package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.dtos.CreatePostRequest;
import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.entity.User;
import com.margaretnjoki.mini_blog_api.repository.PostRepository;
import com.margaretnjoki.mini_blog_api.repository.TagRepository;
import com.margaretnjoki.mini_blog_api.security.CurrentUserProvider;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private CurrentUserProvider currentUserProvider;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Post create (CreatePostRequest req){
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
        return postRepository.save(post);
    }

    public Post findBySlug(String slug){
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new ConfigDataResourceNotFoundException("Post", slug));
    }

    private Post findOwnedById(UUID id){
        UUID userId  =currentUserProvider.getCurrentUser().getId();
        Post post = PostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", slug));
        if (!post)
    }



}
