package sn.dakarterminal.dt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDto {
    private Long id;
    private String userEmail;
    private String userName;
    private String action;
    private String entityType;
    private Long entityId;
    private String details;
    private String ipAddress;
    private String status;
    private LocalDateTime createdAt;
}
