package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "edi_records")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdiRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_type", length = 50)
    private String messageType;

    @Column(name = "sender", length = 100)
    private String sender;

    @Column(name = "receiver", length = 100)
    private String receiver;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "statut", length = 50)
    @Builder.Default
    private String statut = "RECU";

    @Column(name = "processed", nullable = false)
    @Builder.Default
    private Boolean processed = false;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
