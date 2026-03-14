package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.ScanToken;
import sn.dakarterminal.dt.repository.ScanTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanTokenService {

    private final ScanTokenRepository scanTokenRepository;

    @Transactional
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        ScanToken st = ScanToken.builder()
                .token(token)
                .entityType("GFA_TICKET")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();
        scanTokenRepository.save(st);
        return token;
    }

    @Transactional(readOnly = true)
    public boolean isValid(String token) {
        if (token == null) return false;
        return scanTokenRepository.findByToken(token)
                .map(t -> !t.getUsed() && t.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Transactional
    public boolean useToken(String token) {
        if (token == null) return false;
        return scanTokenRepository.findByToken(token)
                .filter(t -> !t.getUsed() && t.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(t -> {
                    t.setUsed(true);
                    t.setUsedAt(LocalDateTime.now());
                    scanTokenRepository.save(t);
                    return true;
                })
                .orElse(false);
    }
}
