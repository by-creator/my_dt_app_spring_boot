package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_accounts")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "numero_compte", unique = true, length = 50)
    private String numeroCompte;

    @Column(name = "solde", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal solde = BigDecimal.ZERO;

    @Column(name = "credit_limite", precision = 15, scale = 2)
    private BigDecimal creditLimite;

    @Column(name = "devise", length = 10)
    @Builder.Default
    private String devise = "XOF";

    @Column(name = "actif", nullable = false)
    @Builder.Default
    private Boolean actif = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
