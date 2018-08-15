package com.example.demo.service.impl;

import com.example.demo.dao.TicketRepository;
import com.example.demo.entity.Ticket;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void save(Ticket ticket) {
        this.ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findTicketsByPersonName(String personName) {
        return this.ticketRepository.findTicketsByPersonName(personName);
    }

    @Override
    public Ticket findTicketById(Long id) {
        return this.ticketRepository.findById(id).get();
    }
}
