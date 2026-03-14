package sn.dakarterminal.dt.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sn.dakarterminal.dt.entity.Ticket;

@Getter
public class TicketClosedEvent extends ApplicationEvent {
    private final Ticket ticket;

    public TicketClosedEvent(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }
}
