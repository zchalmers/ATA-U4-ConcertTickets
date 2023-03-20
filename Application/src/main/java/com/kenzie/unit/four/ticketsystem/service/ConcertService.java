package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.config.CacheStore;
import com.kenzie.unit.four.ticketsystem.repositories.ConcertRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.ConcertRecord;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {
    private ConcertRepository concertRepository;
    private CacheStore cache;

    @Autowired
    public ConcertService(ConcertRepository concertRepository, CacheStore cache) {
        this.concertRepository = concertRepository;
        this.cache = cache;
    }

    public List<Concert> findAllConcerts() {
        List<Concert> concerts = new ArrayList<>();

        Iterable<ConcertRecord> concertIterator = concertRepository.findAll();
        for(ConcertRecord record : concertIterator) {
            concerts.add(new Concert(record.getId(),
                    record.getName(),
                    record.getDate(),
                    record.getTicketBasePrice(),
                    record.getReservationClosed()));
        }

        return concerts;
    }

    public Concert findByConcertId(String concertId) {
        Concert cachedConcert = cache.get(concertId);
        if (cachedConcert != null) {
            return cachedConcert;
        }

        Concert concertBackend = concertRepository
                .findById(concertId)
                .map(concert -> new Concert(concert.getId(),
        concert.getName(),
                        concert.getDate(),
                        concert.getTicketBasePrice(),
                        concert.getReservationClosed()))
                .orElse(null);
                if (concertBackend != null){
                    cache.add(concertBackend.getId(), concertBackend);
                }
               return concertBackend;
    }

    public Concert addNewConcert(Concert concert) {
        ConcertRecord concertRecord = new ConcertRecord();
        concertRecord.setId(concert.getId());
        concertRecord.setDate(concert.getDate());
        concertRecord.setName(concert.getName());
        concertRecord.setTicketBasePrice(concert.getTicketBasePrice());
        concertRecord.setReservationClosed(concert.getReservationClosed());
        concertRepository.save(concertRecord);
        return concert;
    }

    public void updateConcert(Concert concert) {
        cache.evict(concert.getId());
        if (concertRepository.existsById(concert.getId())) {
            ConcertRecord concertRecord = new ConcertRecord();
            concertRecord.setId(concert.getId());
            concertRecord.setDate(concert.getDate());
            concertRecord.setName(concert.getName());
            concertRecord.setTicketBasePrice(concert.getTicketBasePrice());
            concertRecord.setReservationClosed(concert.getReservationClosed());
            concertRepository.save(concertRecord);
        }
    }

    public void deleteConcert(String concertId) {
        concertRepository.deleteById(concertId);
        cache.evict(concertId);
        // Your code here
    }
}
