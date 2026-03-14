package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturations")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facturation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_bl", length = 100)
    private String numeroBl;

    @Column(name = "numero_facture", length = 100)
    private String numeroFacture;

    @Column(name = "numero_proforma", length = 100)
    private String numeroProforma;

    @Column(name = "navire", length = 100)
    private String navire;

    @Column(name = "armateur", length = 150)
    private String armateur;

    @Column(name = "consignataire", length = 150)
    private String consignataire;

    @Column(name = "client", length = 150)
    private String client;

    @Column(name = "code_client", length = 50)
    private String codeClient;

    @Column(name = "nombre_conteneurs")
    private Integer nombreConteneurs;

    @Column(name = "type_conteneur", length = 50)
    private String typeConteneur;

    @Column(name = "poids", precision = 15, scale = 3)
    private BigDecimal poids;

    @Column(name = "volume", precision = 15, scale = 3)
    private BigDecimal volume;

    @Column(name = "montant_ht", precision = 15, scale = 2)
    private BigDecimal montantHt;

    @Column(name = "montant_tva", precision = 15, scale = 2)
    private BigDecimal montantTva;

    @Column(name = "montant_ttc", precision = 15, scale = 2)
    private BigDecimal montantTtc;

    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;

    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "date_proforma")
    private LocalDate dateProforma;

    @Column(name = "date_facture")
    private LocalDate dateFacture;

    @Column(name = "statut", length = 50)
    private String statut;

    @Column(name = "terminal", length = 100)
    private String terminal;

    @Column(name = "voyage", length = 50)
    private String voyage;

    @Column(name = "origine", length = 100)
    private String origine;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "regime", length = 50)
    private String regime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
