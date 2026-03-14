package sn.dakarterminal.dt.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sn.dakarterminal.dt.entity.Ticket;

@Getter
public class TicketCalledEvent extends ApplicationEvent {
    private final Ticket ticket;

    public TicketCalledEvent(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }
}
