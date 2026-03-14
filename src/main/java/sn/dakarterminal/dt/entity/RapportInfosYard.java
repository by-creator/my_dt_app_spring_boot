package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rapport_infos_yards")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportInfosYard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periode_debut")
    private LocalDate periodeDebut;

    @Column(name = "periode_fin")
    private LocalDate periodeFin;

    @Column(name = "total_conteneurs")
    private Integer totalConteneurs;

    @Column(name = "total_plein")
    private Integer totalPlein;

    @Column(name = "total_vide")
    private Integer totalVide;

    @Column(name = "generated_by")
    private Long generatedBy;

    @Column(name = "file_path")
    private String filePath;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
