package com.margaretnjoki.mini_blog_api.dtos;


import com.margaretnjoki.mini_blog_api.entity.User;

import java.util.UUID;

public record UserResponse(UUID id, String email) {
    public static UserResponse from (User u){
        return new UserResponse(u.getId(), u.getEmail());
    }
}
