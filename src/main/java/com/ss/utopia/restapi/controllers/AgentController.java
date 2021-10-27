package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AgentRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.BookingAgent;
import com.ss.utopia.restapi.models.User;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/booking-agents")
public class AgentController {

    @Autowired
    AgentRepository bookingAgentDB;

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping(path="/{id}")
    public ResponseEntity<BookingAgent> getBookingAgent(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(bookingAgentDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingAgent not found!")),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<BookingAgent>> getAllBookingAgents() {
        return new ResponseEntity<>(
            bookingAgentDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> createBookingAgent(@PathVariable int id, @RequestBody User agent) {
        BookingAgent bookingAgent = new BookingAgent();
        Booking booking = bookingRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));

        bookingAgent.setBooking(booking);
        bookingAgent.setAgent(agent);

        try {
            return new ResponseEntity<>(bookingAgentDB.save(bookingAgent), HttpStatus.CREATED);
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

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateBookingAgent(@PathVariable int id, @RequestBody User agent) throws ResponseStatusException {
        BookingAgent bookingAgent = bookingAgentDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingAgent not found!")
        );

        bookingAgent.setAgent(agent);

        try {
            BookingAgent updatedBookingAgent = bookingAgentDB.save(bookingAgent);
            return new ResponseEntity<>(updatedBookingAgent, HttpStatus.NO_CONTENT);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingAgent(@PathVariable int id) throws ResponseStatusException {
        BookingAgent bookingAgent = bookingAgentDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingAgent could not be found!"));

        try {
            bookingAgentDB.delete(bookingAgent);
            return new ResponseEntity<>(bookingAgent, HttpStatus.NO_CONTENT);
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
