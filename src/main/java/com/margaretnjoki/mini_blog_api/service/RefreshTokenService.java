package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.entity.RefreshToken;
import com.margaretnjoki.mini_blog_api.entity.User;
import com.margaretnjoki.mini_blog_api.repository.RefreshTokenRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 30;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }
    public String createRefreshToken(User user){
        String rawToken = generateSecureRandomToken();
        String hash = hash(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hash)
                .expiresAt(Instant.now().plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS))
                .revoked(false)
                .createdAt(Instant.now())
                .build();
        repository.save(refreshToken);

        return rawToken;
    }

    public User verifyAndRotate(String rawToken){
        String hash = hash(rawToken);
        RefreshToken stored = repository.findByTokenHash(hash)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())){
            throw new BadCredentialsException("Refresh token expired or revoked");
        }
        stored.setRevoked(true);
        repository.save(stored);

        return stored.getUser();
    }
    public void revokeAllForUser(User user){
        List<RefreshToken> tokens = repository.findByUserAndRevokedFalse(user);
        tokens.forEach(t -> t.setRevoked(true));
        repository.saveAll(tokens);
    }
    private String generateSecureRandomToken(){
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hash(String token){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalStateException(e);
        }
    }
}
