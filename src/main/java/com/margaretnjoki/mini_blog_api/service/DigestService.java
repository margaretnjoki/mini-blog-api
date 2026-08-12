package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.entity.Post;
import com.margaretnjoki.mini_blog_api.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class DigestService {

    private final PostRepository postRepository;
    private final JavaMailSender mailSender;

    public DigestService(PostRepository postRepository, JavaMailSender mailSender) {
        this.postRepository = postRepository;
        this.mailSender = mailSender;
    }

    public void sendDailyDigest() {
        Instant since = Instant.now().minus(Duration.ofDays(2));
        List<Post> recentPosts = postRepository.findByPublishedAtAfter(since);

        if (recentPosts.isEmpty()) {
            log.info("No new posts in the last 24 hours - skipping digest");
            return;
        }

        String body = buildDigestBody(recentPosts);
        sendEmail("digest@mini-blog.local", "admin@mini-blog.local",
                "Daily Digest - " + recentPosts.size() + " new posts", body);

        log.info("Daily digest sent with {} posts", recentPosts.size());
    }

    private String buildDigestBody(List<Post> posts) {
        StringBuilder sb = new StringBuilder("New posts published today:\n\n");
        for (Post post : posts) {
            sb.append("- ").append(post.getTitle())
                    .append(" by ").append(post.getAuthor().getDisplayName())
                    .append("\n");
        }
        return sb.toString();
    }

    private void sendEmail(String from, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}