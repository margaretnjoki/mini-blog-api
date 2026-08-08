package com.margaretnjoki.mini_blog_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreatePostRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank String bodyMd,
        Set<String> tagNames
) {
}
