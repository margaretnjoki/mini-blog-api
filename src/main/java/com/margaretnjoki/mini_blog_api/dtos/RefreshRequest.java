package com.margaretnjoki.mini_blog_api.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String refreshToken) {
}

