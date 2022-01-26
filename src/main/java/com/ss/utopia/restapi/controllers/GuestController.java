package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.GuestRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.BookingGuest;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// Data transfer object
class Guest {
    public int booking;
    public String email;
    public String phone;

    public Guest() {}
}

@RestController
@RequestMapping(path="/booking-guests")
public class GuestController {

    @Autowired
    GuestRepository bookingGuestDB;

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping(path="/{id}")
    public BookingGuest getBookingGuest(@PathVariable int id) throws ResponseStatusException {
        return bookingGuestDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingGuest not found!"));
    }

    @GetMapping(path={"/all", ""})
    public Iterable<BookingGuest> getAllBookingGuests() {
        return bookingGuestDB.findAll();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createBookingGuest(@RequestBody Guest guest) {
        BookingGuest bookingGuest = new BookingGuest();
        Booking booking = bookingRepository.findById(guest.booking)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));

        bookingGuest.setBooking(booking);
        bookingGuest.setEmail(guest.email);
        bookingGuest.setPhone(guest.phone);

        try {
            return new ResponseEntity<>(bookingGuestDB.save(bookingGuest), HttpStatus.CREATED);
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
    public ResponseEntity<?> updateBookingGuest(@PathVariable int id, @RequestBody Guest guest) throws ResponseStatusException {
        BookingGuest bookingGuest = bookingGuestDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingGuest not found!")
        );

        if (guest.email != null) bookingGuest.setEmail(guest.email);
        if (guest.phone != null) bookingGuest.setPhone(guest.phone);

        try {
            BookingGuest updatedBookingGuest = bookingGuestDB.save(bookingGuest);
            return new ResponseEntity<>(updatedBookingGuest, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<?> deleteBookingGuest(@PathVariable int id) throws ResponseStatusException {
        BookingGuest bookingGuest = bookingGuestDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingGuest could not be found!"));

        try {
            bookingGuestDB.delete(bookingGuest);
            return new ResponseEntity<>(bookingGuest, HttpStatus.NO_CONTENT);
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
