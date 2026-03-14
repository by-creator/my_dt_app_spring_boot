package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "yards")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Yard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conteneur", length = 20)
    private String numeroConteneur;

    @Column(name = "type_conteneur", length = 20)
    private String typeConteneur;

    @Column(name = "etat", length = 20)
    private String etat;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "navire", length = 100)
    private String navire;

    @Column(name = "armateur", length = 150)
    private String armateur;

    @Column(name = "client", length = 150)
    private String client;

    @Column(name = "bl", length = 100)
    private String bl;

    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;

    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "date_sortie")
    private LocalDate dateSortie;

    @Column(name = "statut", length = 50)
    @Builder.Default
    private String statut = "EN_PARC";

    @Column(name = "poids")
    private Double poids;

    @Column(name = "iso_code", length = 10)
    private String isoCode;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
