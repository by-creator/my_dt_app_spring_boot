package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.dto.TicketDto;
import sn.dakarterminal.dt.service.TicketService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> create(@RequestBody Map<String, Object> body) {
        Long serviceId = Long.valueOf(body.get("serviceId").toString());
        String nomClient = (String) body.getOrDefault("nomClient", null);
        String motif = (String) body.getOrDefault("motif", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(serviceId, nomClient, motif));
    }

    @PostMapping("/call-next")
    public ResponseEntity<TicketDto> callNext(@RequestBody Map<String, Object> body) {
        Long serviceId = Long.valueOf(body.get("serviceId").toString());
        Long guichetId = Long.valueOf(body.get("guichetId").toString());
        Long agentId = Long.valueOf(body.get("agentId").toString());
        return ResponseEntity.ok(ticketService.callNext(serviceId, guichetId, agentId));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<TicketDto> close(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.close(id));
    }

    @PatchMapping("/{id}/absent")
    public ResponseEntity<TicketDto> markAbsent(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.markAbsent(id));
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<TicketDto>> getAllWaiting() {
        return ResponseEntity.ok(ticketService.findAllWaiting());
    }

    @GetMapping("/waiting/service/{serviceId}")
    public ResponseEntity<List<TicketDto>> getWaitingByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(ticketService.findWaitingByService(serviceId));
    }
}
