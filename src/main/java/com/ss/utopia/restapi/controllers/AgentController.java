package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.AgentRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.dao.UserRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.BookingAgent;
import com.ss.utopia.restapi.models.BookingAgentPK;
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

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = {
        "/user/{userId}/booking/{bookingId}",
        "/booking/{bookingId}/user/{userId}"
    })
    public ResponseEntity<BookingAgent> getBookingAgent(@PathVariable int bookingId, @PathVariable int userId)
            throws ResponseStatusException {
        return new ResponseEntity<>(
            bookingAgentDB.findById(
                new BookingAgentPK(bookingId, userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking Agents not found!")),
                HttpStatus.OK
            );
    }

    @GetMapping(path="/booking/{id}")
    public ResponseEntity<Iterable<BookingAgent>> getBookingAgentByBooking(@PathVariable int id) {
        return new ResponseEntity<>(
            bookingAgentDB.findAllByBookingId(id),
            HttpStatus.OK
        );
    }

    @GetMapping(path="/user/{id}")
    public ResponseEntity<Iterable<BookingAgent>> getBookingAgentsByAgent(@PathVariable int id) {
        return new ResponseEntity<>(
            bookingAgentDB.findAllByAgentId(id),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<BookingAgent>> getAllAgent() {
        return new ResponseEntity<>(
            bookingAgentDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createBookingUser(@RequestBody BookingAgentPK bookingUserPK) {
        BookingAgent bookingAgent = new BookingAgent();
        Booking booking = bookingRepository
            .findById(bookingUserPK.getBooking())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));
        User user = userRepository
            .findById(bookingUserPK.getAgent())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));

        bookingAgent.setBooking(booking);
        bookingAgent.setAgent(user);
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

    @DeleteMapping(path = {
        "/user/{userId}/booking/{bookingId}",
        "/booking/{bookingId}/user/{userId}"
    })
    public ResponseEntity<?> deleteAgent(@PathVariable int bookingId, @PathVariable int userId)
            throws ResponseStatusException {
        BookingAgent bookingAgent = bookingAgentDB
            .findById(new BookingAgentPK(bookingId, userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agent could not be found!"));

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
