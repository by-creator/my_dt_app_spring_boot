package sn.dakarterminal.dt.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sn.dakarterminal.dt.dto.AuditLogDto;
import sn.dakarterminal.dt.entity.AuditLog;
import sn.dakarterminal.dt.entity.User;
import sn.dakarterminal.dt.repository.AuditLogRepository;
import sn.dakarterminal.dt.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Async
    @Transactional
    public void log(String action, String entityType, Long entityId, String details) {
        String email = getCurrentUserEmail();
        String name = getCurrentUserName(email);
        String ip = getClientIp();
        String userAgent = getUserAgent();

        AuditLog entry = AuditLog.builder()
                .userEmail(email)
                .userName(name)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .ipAddress(ip)
                .userAgent(userAgent)
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(entry);
        log.info("AUDIT | user={} | action={} | entity={} | id={} | details={}",
                email, action, entityType, entityId, details);
    }

    @Async
    @Transactional
    public void logCreate(String entityType, Long entityId, String details) {
        log("CREATE", entityType, entityId, details);
    }

    @Async
    @Transactional
    public void logUpdate(String entityType, Long entityId, String changes) {
        log("UPDATE", entityType, entityId, changes);
    }

    @Async
    @Transactional
    public void logDelete(String entityType, Long entityId, String details) {
        log("DELETE", entityType, entityId, details);
    }

    @Async
    @Transactional
    public void logLogin(String email) {
        AuditLog entry = AuditLog.builder()
                .userEmail(email)
                .userName(getCurrentUserName(email))
                .action("LOGIN")
                .entityType("AUTH")
                .details("Connexion réussie")
                .ipAddress(getClientIp())
                .userAgent(getUserAgent())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(entry);
        log.info("AUDIT | user={} | action=LOGIN", email);
    }

    @Async
    @Transactional
    public void logLogout(String email) {
        AuditLog entry = AuditLog.builder()
                .userEmail(email)
                .action("LOGOUT")
                .entityType("AUTH")
                .details("Déconnexion")
                .ipAddress(getClientIp())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(entry);
        log.info("AUDIT | user={} | action=LOGOUT", email);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogDto> search(String userEmail, String action, String entityType,
                                     LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable) {
        return auditLogRepository.search(userEmail, action, entityType, dateFrom, dateTo, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogDto> findAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable).map(this::toDto);
    }

    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return "SYSTEM";
    }

    private String getCurrentUserName(String email) {
        if ("SYSTEM".equals(email)) return "SYSTEM";
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getName).orElse(email);
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return "unknown";
            HttpServletRequest request = attrs.getRequest();
            String xff = request.getHeader("X-Forwarded-For");
            if (xff != null && !xff.isBlank()) {
                return xff.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getUserAgent() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return null;
            return attrs.getRequest().getHeader("User-Agent");
        } catch (Exception e) {
            return null;
        }
    }

    private AuditLogDto toDto(AuditLog log) {
        return AuditLogDto.builder()
                .id(log.getId())
                .userEmail(log.getUserEmail())
                .userName(log.getUserName())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .details(log.getDetails())
                .ipAddress(log.getIpAddress())
                .status(log.getStatus())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
