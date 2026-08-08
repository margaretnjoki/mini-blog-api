package com.margaretnjoki.mini_blog_api.dtos;

public record RegisterRequest(
        String email,
        String password,
        String displayName
) {
}
