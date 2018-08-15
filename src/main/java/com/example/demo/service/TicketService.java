package com.example.demo.service;

import com.example.demo.entity.Ticket;

import java.util.List;

public interface TicketService {
    public void save(Ticket ticket);
    public List<Ticket> findTicketsByPersonName(String personName);
    public Ticket findTicketById(Long id);
}
