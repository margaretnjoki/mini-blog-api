package com.margaretnjoki.mini_blog_api.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
