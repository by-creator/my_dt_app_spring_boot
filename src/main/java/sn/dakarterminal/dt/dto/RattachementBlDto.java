package sn.dakarterminal.dt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RattachementBlDto {
    private Long id;
    private Long userId;
    private String nom;
    private String prenom;

    @Email
    private String email;

    @NotBlank
    private String bl;

    private String compte;
    private String statut;
    private Long timeElapsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
