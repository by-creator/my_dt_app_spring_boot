package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guichets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuichetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @Column(name = "actif", nullable = false)
    @Builder.Default
    private Boolean actif = true;
}
