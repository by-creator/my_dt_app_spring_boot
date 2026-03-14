package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.dto.TicketDto;
import sn.dakarterminal.dt.entity.Agent;
import sn.dakarterminal.dt.entity.GuichetEntity;
import sn.dakarterminal.dt.entity.ServiceEntity;
import sn.dakarterminal.dt.entity.Ticket;
import sn.dakarterminal.dt.enums.StatutTicket;
import sn.dakarterminal.dt.event.TicketCalledEvent;
import sn.dakarterminal.dt.event.TicketClosedEvent;
import sn.dakarterminal.dt.event.TicketCreatedEvent;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.AgentRepository;
import sn.dakarterminal.dt.repository.GuichetRepository;
import sn.dakarterminal.dt.repository.ServiceRepository;
import sn.dakarterminal.dt.repository.TicketRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ServiceRepository serviceRepository;
    private final AgentRepository agentRepository;
    private final GuichetRepository guichetRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TicketDto createTicket(Long serviceId, String nomClient, String motif) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + serviceId));

        String numero = generateNextNumero();

        Ticket ticket = Ticket.builder()
                .service(service)
                .statut(StatutTicket.EN_ATTENTE)
                .numero(numero)
                .nomClient(nomClient)
                .motif(motif)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        eventPublisher.publishEvent(new TicketCreatedEvent(this, saved));
        return toDto(saved);
    }

    public TicketDto callNext(Long serviceId, Long guichetId, Long agentId) {
        List<Ticket> waiting = ticketRepository.findByServiceIdAndStatut(serviceId, StatutTicket.EN_ATTENTE);
        if (waiting.isEmpty()) {
            throw new IllegalStateException("No tickets waiting for service: " + serviceId);
        }

        Ticket ticket = waiting.get(0);
        GuichetEntity guichet = guichetRepository.findById(guichetId)
                .orElseThrow(() -> new ResourceNotFoundException("Guichet not found: " + guichetId));
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found: " + agentId));

        ticket.setStatut(StatutTicket.EN_COURS);
        ticket.setGuichet(guichet);
        ticket.setAgent(agent);
        ticket.setCalledAt(LocalDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        eventPublisher.publishEvent(new TicketCalledEvent(this, saved));
        return toDto(saved);
    }

    public TicketDto close(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));

        ticket.setStatut(StatutTicket.TERMINE);
        ticket.setClosedAt(LocalDateTime.now());

        if (ticket.getCalledAt() != null) {
            ticket.setProcessingTime(ChronoUnit.SECONDS.between(ticket.getCalledAt(), ticket.getClosedAt()));
        }

        Ticket saved = ticketRepository.save(ticket);
        eventPublisher.publishEvent(new TicketClosedEvent(this, saved));
        return toDto(saved);
    }

    public TicketDto markAbsent(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
        ticket.setStatut(StatutTicket.ABSENT);
        return toDto(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findWaitingByService(Long serviceId) {
        return ticketRepository.findByServiceIdAndStatut(serviceId, StatutTicket.EN_ATTENTE).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllWaiting() {
        return ticketRepository.findByStatutOrderByCreatedAtAsc(StatutTicket.EN_ATTENTE).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private String generateNextNumero() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIDNIGHT);
        int maxNumero = ticketRepository.findMaxNumeroToday(startOfDay).orElse(0);
        return String.format("%03d", maxNumero + 1);
    }

    public TicketDto toDto(Ticket t) {
        return TicketDto.builder()
                .id(t.getId())
                .serviceId(t.getService() != null ? t.getService().getId() : null)
                .serviceNom(t.getService() != null ? t.getService().getNom() : null)
                .agentId(t.getAgent() != null ? t.getAgent().getId() : null)
                .agentNom(t.getAgent() != null ? t.getAgent().getNom() + " " + t.getAgent().getPrenom() : null)
                .guichetId(t.getGuichet() != null ? t.getGuichet().getId() : null)
                .guichetNumero(t.getGuichet() != null ? t.getGuichet().getNumero() : null)
                .statut(t.getStatut())
                .numero(t.getNumero())
                .nomClient(t.getNomClient())
                .motif(t.getMotif())
                .calledAt(t.getCalledAt())
                .closedAt(t.getClosedAt())
                .processingTime(t.getProcessingTime())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
