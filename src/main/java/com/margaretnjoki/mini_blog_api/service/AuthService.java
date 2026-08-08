package com.margaretnjoki.mini_blog_api.service;

import com.margaretnjoki.mini_blog_api.dtos.RegisterRequest;
import com.margaretnjoki.mini_blog_api.entity.User;
import com.margaretnjoki.mini_blog_api.exception.EmailAlreadyInUseException;
import com.margaretnjoki.mini_blog_api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }

    public User register(RegisterRequest request){
        log.info("create user called");
        if (userRepository.findByEmail(request.email()).isPresent()){
            log.warn("user with this email exists {}", request.email());
            throw new EmailAlreadyInUseException(request.email());
        }
        User user= User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .createdAt(Instant.now())
                .build();
        User savedUser = userRepository.save(user);
        log.info("user created successfully id= {}",savedUser.getId());
        notificationService.sendWelcomeNotification(savedUser.getEmail());
        return savedUser;
    }
}

