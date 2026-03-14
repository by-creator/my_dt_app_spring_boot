package sn.dakarterminal.dt.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import sn.dakarterminal.dt.dto.TicketDto;
import sn.dakarterminal.dt.service.TicketService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TicketWebSocketHandler {

    private final TicketService ticketService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ticket/queue/{serviceId}")
    @SendTo("/topic/service/{serviceId}/queue")
    public List<TicketDto> getQueue(@DestinationVariable Long serviceId) {
        return ticketService.findWaitingByService(serviceId);
    }

    @MessageMapping("/ticket/all-queue")
    @SendTo("/topic/all-queue")
    public List<TicketDto> getAllQueue() {
        return ticketService.findAllWaiting();
    }

    public void broadcastTicketUpdate(TicketDto ticket) {
        messagingTemplate.convertAndSend("/topic/tickets/update", ticket);
        if (ticket.getServiceId() != null) {
            messagingTemplate.convertAndSend(
                    "/topic/service/" + ticket.getServiceId() + "/queue", ticket);
        }
    }
}
