package sn.dakarterminal.dt.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sn.dakarterminal.dt.dto.TicketDto;
import sn.dakarterminal.dt.event.TicketCalledEvent;
import sn.dakarterminal.dt.event.TicketClosedEvent;
import sn.dakarterminal.dt.event.TicketCreatedEvent;
import sn.dakarterminal.dt.service.TicketService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final TicketService ticketService;

    @Async
    @EventListener
    public void onTicketCreated(TicketCreatedEvent event) {
        TicketDto dto = ticketService.toDtoById(event.getTicket().getId());
        if (dto == null) return;
        log.info("Ticket created: {} for service: {}", dto.getNumero(),
                dto.getServiceNom());
        messagingTemplate.convertAndSend("/topic/tickets/created", dto);
        messagingTemplate.convertAndSend(
                "/topic/service/" + (dto.getServiceId() != null ? dto.getServiceId() : "all") + "/queue",
                dto);
    }

    @Async
    @EventListener
    public void onTicketCalled(TicketCalledEvent event) {
        TicketDto dto = ticketService.toDtoById(event.getTicket().getId());
        if (dto == null) return;
        log.info("Ticket called: {} at guichet: {}", dto.getNumero(), dto.getGuichetNumero());
        // Broadcast to display screen (all services)
        messagingTemplate.convertAndSend("/topic/tickets/called", dto);
        messagingTemplate.convertAndSend("/topic/display/all", dto);
        if (dto.getServiceId() != null) {
            messagingTemplate.convertAndSend("/topic/display/" + dto.getServiceId(), dto);
            // Broadcast updated waiting queue to guichet page
            messagingTemplate.convertAndSend("/topic/service/" + dto.getServiceId() + "/queue",
                    ticketService.findWaitingByService(dto.getServiceId()));
        }
    }

    @Async
    @EventListener
    public void onTicketClosed(TicketClosedEvent event) {
        TicketDto dto = ticketService.toDtoById(event.getTicket().getId());
        if (dto == null) return;
        log.info("Ticket closed: {} status: {} processing time: {}s",
                dto.getNumero(), dto.getStatut(), dto.getProcessingTime());
        messagingTemplate.convertAndSend("/topic/tickets/closed", dto);
        if (dto.getServiceId() != null) {
            // Broadcast updated waiting queue so guichet page count refreshes
            messagingTemplate.convertAndSend("/topic/service/" + dto.getServiceId() + "/queue",
                    ticketService.findWaitingByService(dto.getServiceId()));
        }
    }
}
