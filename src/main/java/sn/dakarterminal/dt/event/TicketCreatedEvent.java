package sn.dakarterminal.dt.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sn.dakarterminal.dt.entity.Ticket;

@Getter
public class TicketCreatedEvent extends ApplicationEvent {
    private final Ticket ticket;

    public TicketCreatedEvent(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }
}
