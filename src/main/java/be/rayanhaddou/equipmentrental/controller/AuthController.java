package be.rayanhaddou.equipmentrental.controller;

import be.rayanhaddou.equipmentrental.dto.LoginRequest;
import be.rayanhaddou.equipmentrental.dto.RegisterRequest;
import be.rayanhaddou.equipmentrental.security.LoginRateLimiter;
import be.rayanhaddou.equipmentrental.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final LoginRateLimiter loginRateLimiter;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          LoginRateLimiter loginRateLimiter) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.loginRateLimiter = loginRateLimiter;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        String ip = getClientIp(httpRequest);
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        String key = ip + "|" + email;

        if (!loginRateLimiter.tryConsume(key)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many login attempts. Please wait a moment and try again.");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );

            // ✅ voorkomt session fixation
            httpRequest.changeSessionId();

            SecurityContextHolder.getContext().setAuthentication(auth);
            httpRequest.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            loginRateLimiter.reset(key);
            return ResponseEntity.ok().build();

        } catch (org.springframework.security.core.AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null) {
            forwarded = forwarded.trim();
            if (!forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}