package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.FlightBookingsRepository;
import com.ss.utopia.restapi.dao.FlightRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.Flight;
import com.ss.utopia.restapi.models.FlightBookingPK;
import com.ss.utopia.restapi.models.FlightBookings;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/flight-bookings")
public class FlightBookingsController {

    @Autowired
    FlightBookingsRepository flightBookingsDB;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    FlightRepository flightsDB;

    @GetMapping(path = {
        "/flight/{flightId}/booking/{bookingId}",
        "/booking/{bookingId}/flight/{flightId}"
    })
    public ResponseEntity<FlightBookings> getFlightBookings(@PathVariable int bookingId, @PathVariable int flightId)
            throws ResponseStatusException {
        return new ResponseEntity<>(
            flightBookingsDB.findById(
                new FlightBookingPK(flightId, bookingId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "FlightBookings not found!")),
                HttpStatus.OK
            );
    }

    @GetMapping(path="/booking/{id}")
    public ResponseEntity<Iterable<FlightBookings>> getFlightBookingsByBookings(@PathVariable int id) {
        return new ResponseEntity<>(
            flightBookingsDB.findAllByBookingId(id),
            HttpStatus.OK
        );
    }

    @GetMapping(path="/flight/{id}")
    public ResponseEntity<Iterable<FlightBookings>> getFlightBookingsByFlights(@PathVariable int id) {
        return new ResponseEntity<>(
            flightBookingsDB.findAllByFlightId(id),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<FlightBookings>> getAllFlightBookingss() {
        return new ResponseEntity<>(
            flightBookingsDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createFlightBookings(@RequestBody FlightBookingPK flightBookingPK) {
        Booking booking = bookingRepository
            .findById(flightBookingPK.getBooking())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));
        Flight flight = flightsDB
            .findById(flightBookingPK.getFlight())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight not found!"));

        try {
            return new ResponseEntity<>(flightBookingsDB.save(new FlightBookings(flight, booking)), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }
    }

    @DeleteMapping(path={
        "/flight/{flightId}/booking/{bookingId}",
        "/booking/{bookingId}/flight/{flightId}"
    })
    public ResponseEntity<?> deleteFlightBookings(@PathVariable int bookingId, @PathVariable int flightId)
            throws ResponseStatusException {
        FlightBookings flightBookings = flightBookingsDB
            .findById(new FlightBookingPK(flightId, bookingId))
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "FlightBookings could not be found!")
            );

        try {
            flightBookingsDB.delete(flightBookings);
            return new ResponseEntity<>(flightBookings, HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }
    }
}
