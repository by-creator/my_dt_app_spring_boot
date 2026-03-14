package sn.dakarterminal.dt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.dakarterminal.dt.enums.StatutTicket;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private Long id;
    private Long serviceId;
    private String serviceNom;
    private Long agentId;
    private String agentNom;
    private Long guichetId;
    private String guichetNumero;
    private StatutTicket statut;
    private String numero;
    private String nomClient;
    private String motif;
    private LocalDateTime calledAt;
    private LocalDateTime closedAt;
    private Long processingTime;
    private LocalDateTime createdAt;
}
