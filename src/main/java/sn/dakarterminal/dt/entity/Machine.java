package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.dakarterminal.dt.enums.TypeMachine;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_serie", unique = true, length = 100)
    private String numeroSerie;

    @Column(name = "modele", length = 100)
    private String modele;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, columnDefinition = "VARCHAR(50)")
    private TypeMachine type;

    @Column(name = "utilisateur", length = 150)
    private String utilisateur;

    @Column(name = "service", length = 100)
    private String service;

    @Column(name = "site", length = 100)
    private String site;

    @Column(name = "marque", length = 100)
    private String marque;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Column(name = "date_garantie")
    private LocalDate dateGarantie;

    @Column(name = "etat", length = 50)
    @Builder.Default
    private String etat = "EN_SERVICE";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
