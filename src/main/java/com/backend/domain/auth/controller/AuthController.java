package com.backend.domain.auth.controller;

import com.backend.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshJwtAndRotate(
            @CookieValue("refresh") Cookie refreshTokenCookie, HttpServletResponse response) {
        authService.refreshJwtAndRotate(refreshTokenCookie, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("access") Cookie accessTokenCookie,
            @CookieValue("refresh") Cookie refreshTokenCookie,
            HttpServletResponse response) {
        authService.logout(accessTokenCookie, refreshTokenCookie, response);
        return ResponseEntity.ok().build();
    }
}
