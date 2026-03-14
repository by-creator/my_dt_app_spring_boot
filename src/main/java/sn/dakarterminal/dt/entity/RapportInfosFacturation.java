package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapport_infos_facturations")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportInfosFacturation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periode_debut")
    private LocalDate periodeDebut;

    @Column(name = "periode_fin")
    private LocalDate periodeFin;

    @Column(name = "total_factures")
    private Integer totalFactures;

    @Column(name = "total_montant_ht", precision = 15, scale = 2)
    private BigDecimal totalMontantHt;

    @Column(name = "total_montant_tva", precision = 15, scale = 2)
    private BigDecimal totalMontantTva;

    @Column(name = "total_montant_ttc", precision = 15, scale = 2)
    private BigDecimal totalMontantTtc;

    @Column(name = "generated_by")
    private Long generatedBy;

    @Column(name = "file_path")
    private String filePath;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
