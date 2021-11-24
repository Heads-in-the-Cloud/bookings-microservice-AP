package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.services.ResetAutoCounterService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/bookings")
public class BookingController {

    @Autowired
    BookingRepository bookingDB;

    @Autowired
    ResetAutoCounterService resetService;

    @GetMapping(path="/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(bookingDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!")),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<Booking>> getAllBookings() {
        return new ResponseEntity<>(
            bookingDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            resetService.resetAutoCounter("booking");
            return new ResponseEntity<>(bookingDB.save(booking), HttpStatus.CREATED);
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
    public ResponseEntity<?> updateBooking(@PathVariable int id, @RequestBody Booking bookingDetails) throws ResponseStatusException {
        Booking booking = bookingDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!")
        );

        booking.setIsActive(bookingDetails.getIsActive());
        booking.setConfirmationCode(bookingDetails.getConfirmationCode());

        try {
            Booking updatedBooking = bookingDB.save(booking);
            return new ResponseEntity<>(updatedBooking, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<?> deleteBooking(@PathVariable int id) throws ResponseStatusException {
        Booking booking = bookingDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking could not be found!"));

        try {
            bookingDB.delete(booking);
            resetService.resetAutoCounter("booking");
            return new ResponseEntity<>(booking, HttpStatus.NO_CONTENT);
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
