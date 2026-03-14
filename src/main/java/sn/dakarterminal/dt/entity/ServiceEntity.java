package sn.dakarterminal.dt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "code", unique = true, length = 50)
    private String code;

    @Column(name = "actif", nullable = false)
    @Builder.Default
    private Boolean actif = true;
}
