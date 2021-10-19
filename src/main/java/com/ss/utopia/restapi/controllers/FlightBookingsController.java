package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.FlightBookingsRepository;
import com.ss.utopia.restapi.dao.FlightRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.Flight;
import com.ss.utopia.restapi.models.FlightBookingPK;
import com.ss.utopia.restapi.models.FlightBookings;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/booking")
public class FlightBookingsController {

    @Autowired
    FlightBookingsRepository flightBookingsDB;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    FlightRepository flightsDB;

    @GetMapping(path = "/{bookingId}/flight/{flightId}")
    public FlightBookings getFlightBookings(@PathVariable int bookingId, @PathVariable int flightId)
            throws ResponseStatusException {
        return flightBookingsDB.findById(new FlightBookingPK(flightId, bookingId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "FlightBookings not found!"));
    }

    @GetMapping(path="/{id}/flight/all")
    public Iterable<FlightBookings> getFlightBookingsByBookings(@PathVariable int id) {
        return flightBookingsDB.findAllByBookingId(id);
    }

    @GetMapping(path="/all/flight/{id}")
    public Iterable<FlightBookings> getFlightBookingsByFlights(@PathVariable int id) {
        return flightBookingsDB.findAllByFlightId(id);
    }

    @GetMapping(path="/all/flight/all")
    public Iterable<FlightBookings> getAllFlightBookingss() {
        return flightBookingsDB.findAll();
    }

    @PostMapping(path = "/{bookingId}/flight/{flightId}")
    public ResponseEntity<?> createFlightBookings(@PathVariable int bookingId, @PathVariable int flightId) {
        Booking booking = bookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));
        Flight flight = flightsDB
            .findById(flightId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found!"));

        return new ResponseEntity<>(flightBookingsDB.save(new FlightBookings(flight, booking)), HttpStatus.OK);
    }

    @DeleteMapping("/{bookingId}/flight/{flightId}")
    public ResponseEntity<?> deleteFlightBookings(@PathVariable int bookingId, @PathVariable int flightId)
            throws ResponseStatusException {
        FlightBookings flightBookings = flightBookingsDB.findById(new FlightBookingPK(flightId, bookingId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "FlightBookings could not be found!"));

        flightBookingsDB.delete(flightBookings);
        return new ResponseEntity<>(flightBookings, HttpStatus.OK);
    }
}
