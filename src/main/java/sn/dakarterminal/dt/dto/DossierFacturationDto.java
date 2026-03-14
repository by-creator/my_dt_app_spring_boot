package sn.dakarterminal.dt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.dakarterminal.dt.enums.StatutDossier;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DossierFacturationDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long rattachementBlId;
    private String numeroBl;
    private StatutDossier statut;
    private LocalDateTime dateProforma;
    private Long timeElapsedProforma;
    private Long timeElapsedFacture;
    private Long timeElapsedBon;
    private Integer relanceProforma;
    private Integer relanceFacture;
    private Integer relanceBon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
