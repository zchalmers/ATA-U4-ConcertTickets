package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.ReservedTicketRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class ReservedTicketService {
    private ConcertService concertService;
    private ReservedTicketRepository reservedTicketRepository;
    private final ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue;

    public ReservedTicketService(ReservedTicketRepository reservedTicketRepository,
                                 ConcertService concertService,
                                 ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue) {
        this.reservedTicketRepository = reservedTicketRepository;
        this.concertService = concertService;
        this.reservedTicketsQueue = reservedTicketsQueue;
    }

    public List<ReservedTicket> findAllReservationTickets() {
        List<ReservedTicket> reservedTickets = new ArrayList<>();

        Iterable<ReserveTicketRecord> ticketRecords = reservedTicketRepository.findAll();

        for (ReserveTicketRecord record : ticketRecords) {
            reservedTickets.add(new ReservedTicket(record.getConcertId(),
                    record.getTicketId(),
                    record.getDateOfReservation(),
                    record.getReservationClosed(),
                    record.getDateReservationClosed(),
                    record.getPurchasedTicket()));
        }


        return reservedTickets;
    }

    public List<ReservedTicket> findAllUnclosedReservationTickets() {
        List<ReservedTicket> allReservedTickets = findAllReservationTickets();
        List<ReservedTicket> unclosedReservations = new ArrayList<>();

        for (ReservedTicket reservedTicket : allReservedTickets) {
            if ((reservedTicket.getTicketPurchased() == null || !reservedTicket.getTicketPurchased()) &&
                (reservedTicket.getReservationClosed() == null || !reservedTicket.getReservationClosed())) {
                unclosedReservations.add(reservedTicket);
            }
        }
        return unclosedReservations;
    }

    public ReservedTicket reserveTicket(ReservedTicket reservedTicket) {
        // Your code here
        Concert concert = concertService.findByConcertId(reservedTicket.getConcertId());
        if (concert == null || concert.getReservationClosed() ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Concert does not exist or is closed.");
        }
        ReserveTicketRecord reserveTicketRecord = new ReserveTicketRecord();
        reserveTicketRecord.setTicketId(reservedTicket.getTicketId());
        reserveTicketRecord.setConcertId(reservedTicket.getConcertId());
        reserveTicketRecord.setDateOfReservation(reservedTicket.getDateOfReservation());
        reserveTicketRecord.setDateReservationClosed(LocalDateTime.now().toString());
        reserveTicketRecord.setReservationClosed(false);
        reserveTicketRecord.setPurchasedTicket(reservedTicket.getTicketPurchased());
        reservedTicketRepository.save(reserveTicketRecord);

        reservedTicketsQueue.add(reservedTicket);
        return reservedTicket;
    }

    public ReservedTicket findByReserveTicketId(String reserveTicketId) {
        Optional<ReserveTicketRecord> ticketRecordOptional = reservedTicketRepository.findById(reserveTicketId);
        if (ticketRecordOptional.isPresent()) {
            ReserveTicketRecord record = ticketRecordOptional.get();
            return new ReservedTicket(record.getConcertId(),
                    record.getTicketId(),
                    record.getDateOfReservation(),
                    record.getReservationClosed(),
                    record.getDateReservationClosed(),
                    record.getPurchasedTicket());
        } else {
            return null;
        }
    }

    public List<ReservedTicket> findByConcertId(String concertId) {
        // Your code here
        List<ReserveTicketRecord> ticketRecords = reservedTicketRepository.findByConcertId(concertId);
        List<ReservedTicket> reservedList = new ArrayList<>();
        ticketRecords.forEach(x -> reservedList.add(new ReservedTicket(x.getConcertId(),
                x.getTicketId(), x.getDateOfReservation())));

        return reservedList;
    }

    public ReservedTicket updateReserveTicket(ReservedTicket reservedTicket) {
        ReserveTicketRecord reserveTicketRecord = new ReserveTicketRecord();
        reserveTicketRecord.setTicketId(reservedTicket.getTicketId());
        reserveTicketRecord.setConcertId(reservedTicket.getConcertId());
        reserveTicketRecord.setDateOfReservation(reservedTicket.getDateOfReservation());
        reserveTicketRecord.setDateReservationClosed(reservedTicket.getDateReservationClosed());
        reserveTicketRecord.setReservationClosed(reservedTicket.getReservationClosed());
        reserveTicketRecord.setPurchasedTicket(reservedTicket.getTicketPurchased());
        reservedTicketRepository.save(reserveTicketRecord);
        return reservedTicket;
    }
}
