package com.ss.utopia.restapi.dao;

import java.util.List;

import com.ss.utopia.restapi.models.*;

import org.springframework.data.repository.CrudRepository;

public interface FlightBookingsRepository extends CrudRepository<FlightBookings, FlightBookingPK> {
    public List<FlightBookings> findAllByBookingId(int booking);
    public List<FlightBookings> findAllByFlightId(int flight);
}