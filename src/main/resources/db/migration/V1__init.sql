CREATE TABLE users (
                       id            UUID PRIMARY KEY,
                       email         VARCHAR(150) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       display_name  VARCHAR(100) NOT NULL,
                       bio           VARCHAR(500),
                       created_at    TIMESTAMPTZ NOT NULL
);

CREATE TABLE posts (
                       id           UUID PRIMARY KEY,
                       author_id    UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       title        VARCHAR(200) NOT NULL,
                       slug         VARCHAR(220) NOT NULL UNIQUE,
                       body_md      TEXT NOT NULL,
                       published_at TIMESTAMPTZ,
                       created_at   TIMESTAMPTZ NOT NULL
);

CREATE TABLE comments (
                          id         UUID PRIMARY KEY,
                          post_id    UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                          author_id  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          body       VARCHAR(2000) NOT NULL,
                          created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE tags (
                      id   UUID PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE posts_tags (
                            post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                            tag_id  UUID NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
                            PRIMARY KEY (post_id, tag_id)
);

CREATE INDEX idx_posts_author ON posts(author_id);
CREATE INDEX idx_posts_slug ON posts(slug);
CREATE INDEX idx_comments_post ON comments(post_id);