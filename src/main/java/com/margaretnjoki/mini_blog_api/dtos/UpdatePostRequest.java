package com.margaretnjoki.mini_blog_api.dtos;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdatePostRequest(
        @Size(max = 200) String title,
        String bodyMd,
        Boolean published,
        Set<String> tagNames
) {
}
