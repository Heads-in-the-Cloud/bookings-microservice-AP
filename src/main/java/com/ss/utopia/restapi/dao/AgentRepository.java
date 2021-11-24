package com.ss.utopia.restapi.dao;

import java.util.List;

import com.ss.utopia.restapi.models.*;

import org.springframework.data.repository.CrudRepository;

public interface AgentRepository extends CrudRepository<BookingAgent, BookingAgentPK> {
    public List<BookingAgent> findAllByBookingId(int booking);
    public List<BookingAgent> findAllByAgentId(int agent);
}