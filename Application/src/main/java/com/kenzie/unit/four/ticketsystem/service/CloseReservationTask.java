package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CloseReservationTask implements Runnable {

    private final Integer durationToPay;
    private final ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue;
    private final ReservedTicketService reservedTicketService;

    public CloseReservationTask(Integer durationToPay,
                                ReservedTicketService reservedTicketService,
                                ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue) {
        this.durationToPay = durationToPay;
        this.reservedTicketService = reservedTicketService;
        this.reservedTicketsQueue = reservedTicketsQueue;
    }

    @Override
    public void run() {
       // Your code here
        ReservedTicket ticket = reservedTicketsQueue.poll();
        if (ticket == null){
//            ticket = reservedTicketService.findByReserveTicketId()
        }
        Duration duration  = Duration.between(LocalDateTime.parse(ticket.getDateReservationClosed()), LocalDateTime.now());
        if (!ticket.getTicketPurchased() && duration.getSeconds() > durationToPay){
            ReservedTicket reservedTicket = new ReservedTicket(ticket.getConcertId(), ticket.getTicketId(), ticket.getDateOfReservation(), true, LocalDateTime.now().toString(), false);
            reservedTicketService.updateReserveTicket(reservedTicket);
        }
        else {
            reservedTicketsQueue.add(ticket);
        }
    }
}
