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
        TicketDto dto = ticketService.toDto(event.getTicket());
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
        TicketDto dto = ticketService.toDto(event.getTicket());
        log.info("Ticket called: {} at guichet: {}", dto.getNumero(), dto.getGuichetNumero());
        messagingTemplate.convertAndSend("/topic/tickets/called", dto);
        messagingTemplate.convertAndSend(
                "/topic/display/" + (dto.getServiceId() != null ? dto.getServiceId() : "all"),
                dto);
    }

    @Async
    @EventListener
    public void onTicketClosed(TicketClosedEvent event) {
        TicketDto dto = ticketService.toDto(event.getTicket());
        log.info("Ticket closed: {} processing time: {}s", dto.getNumero(), dto.getProcessingTime());
        messagingTemplate.convertAndSend("/topic/tickets/closed", dto);
    }
}
