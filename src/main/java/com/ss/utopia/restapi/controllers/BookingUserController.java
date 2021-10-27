package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.BookingUserRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.BookingUser;
import com.ss.utopia.restapi.models.User;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/booking-users")
public class BookingUserController {

    @Autowired
    BookingUserRepository bookingUserDB;

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping(path="/{id}")
    public ResponseEntity<BookingUser> getBookingUser(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(bookingUserDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingUser not found!")),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<BookingUser>> getAllBookingUsers() {
        return new ResponseEntity<>(
            bookingUserDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> createBookingUser(@PathVariable int id, @RequestBody User user) {
        BookingUser bookingUser = new BookingUser();
        Booking booking = bookingRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));

        bookingUser.setBooking(booking);
        bookingUser.setUser(user);
        try {
            return new ResponseEntity<>(bookingUserDB.save(bookingUser), HttpStatus.CREATED);
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
    public ResponseEntity<?> updateBookingUser(@PathVariable int id, @RequestBody BookingUser bookingUserDetails) throws ResponseStatusException {
        BookingUser bookingUser = bookingUserDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingUser not found!")
        );

        bookingUser.setUser(bookingUserDetails.getUser());

        try {
            BookingUser updatedBookingUser = bookingUserDB.save(bookingUser);
            return new ResponseEntity<>(updatedBookingUser, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<?> deleteBookingUser(@PathVariable int id) throws ResponseStatusException {
        BookingUser bookingUser = bookingUserDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingUser could not be found!"));

        try {
            bookingUserDB.delete(bookingUser);
            return new ResponseEntity<>(bookingUser, HttpStatus.NO_CONTENT);
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
