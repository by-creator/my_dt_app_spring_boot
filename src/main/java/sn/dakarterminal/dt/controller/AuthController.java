package sn.dakarterminal.dt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.dto.LoginRequest;
import sn.dakarterminal.dt.dto.LoginResponse;
import sn.dakarterminal.dt.dto.UserDto;
import sn.dakarterminal.dt.security.JwtTokenProvider;
import sn.dakarterminal.dt.service.AuditService;
import sn.dakarterminal.dt.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuditService auditService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(request.getEmail());

        UserDto user = userService.findByEmail(request.getEmail());
        auditService.logLogin(request.getEmail());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(jwtTokenProvider.getJwtExpiration())
                .user(user)
                .twoFactorRequired(Boolean.TRUE.equals(user.getTwoFactorEnabled()))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestParam String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String newToken = jwtTokenProvider.generateTokenFromUsername(username);
        UserDto user = userService.findByEmail(username);

        LoginResponse response = LoginResponse.builder()
                .token(newToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtTokenProvider.getJwtExpiration())
                .user(user)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            auditService.logLogout(auth.getName());
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        return ResponseEntity.ok(userService.findByEmail(authentication.getName()));
    }
}
