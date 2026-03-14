package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordre_approche_stagings")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdreApprocheStaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_oa", length = 100)
    private String numeroOa;

    @Column(name = "navire", length = 100)
    private String navire;

    @Column(name = "armateur", length = 150)
    private String armateur;

    @Column(name = "consignataire", length = 150)
    private String consignataire;

    @Column(name = "terminal", length = 100)
    private String terminal;

    @Column(name = "quai", length = 100)
    private String quai;

    @Column(name = "voyage", length = 50)
    private String voyage;

    @Column(name = "date_arrivee_prevue")
    private LocalDate dateArriveePrevue;

    @Column(name = "date_depart_prevue")
    private LocalDate dateDepartPrevue;

    @Column(name = "statut", length = 50)
    private String statut;

    @Column(name = "batch_id", length = 100)
    private String batchId;

    @Column(name = "import_errors", columnDefinition = "TEXT")
    private String importErrors;

    @Column(name = "processed", nullable = false)
    @Builder.Default
    private Boolean processed = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
