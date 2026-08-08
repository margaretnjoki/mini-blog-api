package com.margaretnjoki.mini_blog_api.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, UUID id) {
        super(resource + " not found: " + id);
    }

    public ResourceNotFoundException(String resource, String slug) {
        super(resource + " not found: " + slug);
    }
}
