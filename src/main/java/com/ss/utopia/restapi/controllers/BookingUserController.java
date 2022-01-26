package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.BookingUserRepository;
import com.ss.utopia.restapi.dao.UserRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.BookingUser;
import com.ss.utopia.restapi.models.BookingUserPK;
import com.ss.utopia.restapi.models.User;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.ss.utopia.restapi.services.GetUserFromAuthHeader.getUserFromAuthHeader;

@RestController
@RequestMapping(path="/booking-users")
public class BookingUserController {

    @Autowired
    BookingUserRepository bookingUserDB;

    @Autowired
    BookingRepository bookingDB;

    @Autowired
    UserRepository userDB;

    @GetMapping(path = {
        "/user/{userId}/booking/{bookingId}",
        "/booking/{bookingId}/user/{userId}"
    })
    public ResponseEntity<BookingUser> getBookingUsers(@PathVariable int bookingId, @PathVariable int userId)
            throws ResponseStatusException {

        User user = getUserFromAuthHeader(userDB);

        if (user.getRole().getName().equals("ADMIN") || user.getId() == userId) {
            return new ResponseEntity<>(
                bookingUserDB.findById(
                    new BookingUserPK(bookingId, userId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingUsers not found!")),
                    HttpStatus.OK
                );
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to see this booking user!");
    }

    @GetMapping(path="/booking/{id}")
    public ResponseEntity<Iterable<BookingUser>> getBookingUsersByBooking(@PathVariable int id) {
        User user = getUserFromAuthHeader(userDB);

        if (user.getRole().getName().equals("ADMIN")) {
            return new ResponseEntity<>(
                bookingUserDB.findAllByBookingId(id),
                HttpStatus.OK
            );
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to see this booking user!");
    }

    @GetMapping(path="/user/{id}")
    public ResponseEntity<Iterable<BookingUser>> getBookingUsersByUser(@PathVariable int id) {
        User user = getUserFromAuthHeader(userDB);

        if (user.getRole().getName().equals("ADMIN") || user.getId() == id) {
            return new ResponseEntity<>(
                bookingUserDB.findAllByUserId(id),
                HttpStatus.OK
            );
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to see these booking users!");
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<BookingUser>> getAllBookingUsers() {
        User user = getUserFromAuthHeader(userDB);

        if (user.getRole().getName().equals("ADMIN")) {
            return new ResponseEntity<>(
                bookingUserDB.findAll(),
                HttpStatus.OK
            );
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to see this user!");
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createBookingUser(@RequestBody BookingUserPK bookingUserPK) {
        BookingUser bookingUser = new BookingUser();
        Booking booking = bookingDB
            .findById(bookingUserPK.getBooking())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));
        User user = userDB
            .findById(bookingUserPK.getUser())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!"));

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

    @DeleteMapping(path = {
        "/user/{userId}/booking/{bookingId}",
        "/booking/{bookingId}/user/{userId}"
    })
    public ResponseEntity<?> deleteBookingUser(@PathVariable int bookingId, @PathVariable int userId)
            throws ResponseStatusException {
        BookingUser bookingUser = bookingUserDB
            .findById(new BookingUserPK(bookingId, userId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingUser could not be found!"));

        User user = getUserFromAuthHeader(userDB);

        if (user.getRole().getName().equals("ADMIN")) {
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

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to delete this user!");
    }
}
