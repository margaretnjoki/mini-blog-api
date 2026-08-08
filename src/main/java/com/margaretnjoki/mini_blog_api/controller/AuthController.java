package com.margaretnjoki.mini_blog_api.controller;

import com.margaretnjoki.mini_blog_api.dtos.*;
import com.margaretnjoki.mini_blog_api.entity.User;
import com.margaretnjoki.mini_blog_api.repository.UserRepository;
import com.margaretnjoki.mini_blog_api.security.JwtService;
import com.margaretnjoki.mini_blog_api.service.AuthService;
import com.margaretnjoki.mini_blog_api.service.RefreshTokenCleanupService;
import com.margaretnjoki.mini_blog_api.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;


    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, RefreshTokenService refreshTokenService, RefreshTokenCleanupService service) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request){
        log.info(">>> REGISTER ENDPOINT HIT <<<");

        return UserResponse.from(authService.register(request));

    }
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("logging user");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            log.info("authenticate");

        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage(), e);
            throw e;
        }
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        log.info("retrieve user by email");
        String accessToken = jwtService.generateToken(request.email());
        String refreshToken = refreshTokenService.createRefreshToken(user);
        log.info("generating access token and refresh token");

        return new LoginResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@Valid @RequestBody RefreshRequest request){
        User user = refreshTokenService.verifyAndRotate(request.refreshToken());
        String newAccessToken = jwtService.generateToken(user.getEmail());
        String newRefreshToken = refreshTokenService.createRefreshToken(user);
        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        refreshTokenService.revokeAllForUser(user);
    }

    @GetMapping("/test")
    public String test() {
        return "Auth controller is working!";
    }
}

