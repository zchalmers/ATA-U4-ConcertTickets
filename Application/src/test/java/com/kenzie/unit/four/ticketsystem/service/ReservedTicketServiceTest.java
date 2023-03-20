package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.ReservedTicketRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.ConcertRecord;
import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.amazonaws.services.s3.internal.eventstreaming.HeaderType.UUID;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservedTicketServiceTest {

    private ReservedTicketRepository reservedTicketRepository;
    private ConcertService concertService;
    private ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue;
    private ReservedTicketService reservedTicketService;

    @BeforeEach
    void setup() {
        reservedTicketRepository = mock(ReservedTicketRepository.class);
        concertService = mock(ConcertService.class);
        reservedTicketsQueue = new ConcurrentLinkedQueue<>();
        reservedTicketService = new ReservedTicketService(reservedTicketRepository, concertService, reservedTicketsQueue);
    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.findAllReservationTickets
     *  ------------------------------------------------------------------------ **/

    @Test
    void findAllReservations() {
        // GIVEN
        ReserveTicketRecord record1 = new ReserveTicketRecord();
        record1.setTicketId(randomUUID().toString());
        record1.setConcertId(randomUUID().toString());
        record1.setDateOfReservation("record1date");
        record1.setDateReservationClosed("closed1date");
        record1.setReservationClosed(false);
        record1.setPurchasedTicket(true);

        ReserveTicketRecord record2 = new ReserveTicketRecord();
        record2.setTicketId(randomUUID().toString());
        record2.setConcertId(randomUUID().toString());
        record2.setDateOfReservation("record2date");
        record2.setDateReservationClosed("closed2date");
        record2.setReservationClosed(true);
        record2.setPurchasedTicket(false);

        List<ReserveTicketRecord> records = new ArrayList<>();

        records.add(record1);
        records.add(record2);

        when(reservedTicketRepository.findAll()).thenReturn(records);
        // WHEN

        List<ReservedTicket> reservations = reservedTicketService.findAllReservationTickets();

        // THEN
        Assertions.assertNotNull(reservations, "The reserved ticket list is returned");
        assertEquals(2, reservations.size(), "There are two reserved tickets");

        for (ReservedTicket ticket : reservations) {
            if (ticket.getTicketId() == record1.getTicketId()) {
                assertEquals(record1.getConcertId(), ticket.getConcertId(), "The concert id matches");
                assertEquals(record1.getDateOfReservation(), ticket.getDateOfReservation(), "The reservation date matches");
                assertEquals(record1.getReservationClosed(), ticket.getReservationClosed(), "The reservationClosed matches");
                assertEquals(record1.getPurchasedTicket(), ticket.getTicketPurchased(), "The ticketPurchased matches");
                assertEquals(record1.getDateReservationClosed(), ticket.getDateReservationClosed(), "The reservation closed date matches");
            } else if (ticket.getTicketId() == record2.getTicketId()) {
                assertEquals(record2.getConcertId(), ticket.getConcertId(), "The concert id matches");
                assertEquals(record2.getDateOfReservation(), ticket.getDateOfReservation(), "The reservation date matches");
                assertEquals(record2.getReservationClosed(), ticket.getReservationClosed(), "The reservationClosed matches");
                assertEquals(record2.getPurchasedTicket(), ticket.getTicketPurchased(), "The ticketPurchased matches");
                assertEquals(record2.getDateReservationClosed(), ticket.getDateReservationClosed(), "The reservation closed date matches");
            } else {
                assertTrue(false, "Reserved Ticket returned that was not in the records!");
            }
        }
    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.findAllUnclosedReservationTickets
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here

    @Test
    void reservedTicketService_findAllIUnclosedReservationTickets() {
        ReserveTicketRecord record1 = new ReserveTicketRecord();
        record1.setTicketId(randomUUID().toString());
        record1.setConcertId(randomUUID().toString());
        record1.setDateOfReservation("record1date");
        record1.setDateReservationClosed("closed1date");
        record1.setReservationClosed(false);
        record1.setPurchasedTicket(false);

        ReserveTicketRecord record2 = new ReserveTicketRecord();
        record2.setTicketId(randomUUID().toString());
        record2.setConcertId(randomUUID().toString());
        record2.setDateOfReservation("record2date");
        record2.setDateReservationClosed("closed2date");
        record2.setReservationClosed(false);
        record2.setPurchasedTicket(false);

        List<ReserveTicketRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);

        when(reservedTicketRepository.findAll()).thenReturn(recordList);

        List<ReservedTicket> testList = reservedTicketService.findAllUnclosedReservationTickets();

        assertEquals(testList.get(0).getTicketId(), record1.getTicketId());
        assertEquals(testList.get(1).getDateOfReservation(), record2.getDateOfReservation());
    }
    /** ------------------------------------------------------------------------
     *  reservedTicketService.reserveTicket
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here
@Test
void reservedTicketService_reserveTicket(){
    ReservedTicket ticket = new ReservedTicket("cocnert", "ticket", "now");
    Concert concert = mock(Concert.class);

    when(concertService.findByConcertId(any())).thenReturn(concert);

    reservedTicketService.reserveTicket(ticket);
    List<ReserveTicketRecord> list = reservedTicketRepository.findByConcertId(ticket.getConcertId());

    assertEquals(ticket.getTicketId(), list.get(0).getTicketId());
    assertEquals(ticket.getDateOfReservation(), list.get(0).getDateOfReservation());
}
    /** ------------------------------------------------------------------------
     *  reservedTicketService.findByReserveTicketId
     *  ------------------------------------------------------------------------ **/

    @Test
    void findByReserveTicketId() {
        // GIVEN
        ReserveTicketRecord record = new ReserveTicketRecord();
        record.setTicketId(randomUUID().toString());
        record.setConcertId(randomUUID().toString());
        record.setDateOfReservation("record2date");
        record.setDateReservationClosed("closed2date");
        record.setReservationClosed(true);
        record.setPurchasedTicket(false);

        when(reservedTicketRepository.findById(record.getTicketId())).thenReturn(Optional.of(record));

        // WHEN
        ReservedTicket reservedTicket = reservedTicketService.findByReserveTicketId(record.getTicketId());

        // THEN
        Assertions.assertNotNull(reservedTicket);
        assertEquals(record.getConcertId(), reservedTicket.getConcertId(), "The concert id matches");
        assertEquals(record.getDateOfReservation(), reservedTicket.getDateOfReservation(), "The reservation date matches");
        assertEquals(record.getReservationClosed(), reservedTicket.getReservationClosed(), "The reservationClosed matches");
        assertEquals(record.getPurchasedTicket(), reservedTicket.getTicketPurchased(), "The ticketPurchased matches");
        assertEquals(record.getDateReservationClosed(), reservedTicket.getDateReservationClosed(), "The reservation closed date matches");
    }


    /** ------------------------------------------------------------------------
     *  reservedTicketService.findByConcertId
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here

    /** ------------------------------------------------------------------------
     *  reservedTicketService.updateReserveTicket
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateReserveTicket() {
        // GIVEN
        ReserveTicketRecord record = new ReserveTicketRecord();
        record.setTicketId(randomUUID().toString());
        record.setConcertId(randomUUID().toString());
        record.setDateOfReservation("record2date");
        record.setDateReservationClosed("closed2date");
        record.setReservationClosed(true);
        record.setPurchasedTicket(false);

        ReservedTicket reservedTicket = new ReservedTicket(
                record.getConcertId(),
                record.getTicketId(),
                record.getDateOfReservation(),
                record.getReservationClosed(),
                record.getDateReservationClosed(),
                record.getPurchasedTicket());

        ArgumentCaptor<ReserveTicketRecord> recordCaptor = ArgumentCaptor.forClass(ReserveTicketRecord.class);

        // WHEN
        reservedTicketService.updateReserveTicket(reservedTicket);

        // THEN
        verify(reservedTicketRepository).save(recordCaptor.capture());
        ReserveTicketRecord storedRecord = recordCaptor.getValue();

        Assertions.assertNotNull(reservedTicket);
        assertEquals(storedRecord.getConcertId(), reservedTicket.getConcertId(), "The concert id matches");
        assertEquals(storedRecord.getDateOfReservation(), reservedTicket.getDateOfReservation(), "The reservation date matches");
        assertEquals(storedRecord.getReservationClosed(), reservedTicket.getReservationClosed(), "The reservationClosed matches");
        assertEquals(storedRecord.getPurchasedTicket(), reservedTicket.getTicketPurchased(), "The ticketPurchased matches");
        assertEquals(storedRecord.getDateReservationClosed(), reservedTicket.getDateReservationClosed(), "The reservation closed date matches");
    }
}
