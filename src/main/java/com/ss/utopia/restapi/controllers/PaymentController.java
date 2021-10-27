package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.PaymentRepository;
import com.ss.utopia.restapi.dao.BookingRepository;
import com.ss.utopia.restapi.models.Booking;
import com.ss.utopia.restapi.models.BookingPayment;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// Data transfer object
class Payment {
    public Boolean refunded;
    public String stripeId;
}

@RestController
@RequestMapping(path="/booking-payments")
public class PaymentController {

    @Autowired
    PaymentRepository bookingPaymentDB;

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping(path="/{id}")
    public ResponseEntity<BookingPayment> getBookingPayment(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(bookingPaymentDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingPayment not found!")),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<BookingPayment>> getAllBookingPayments() {
        return new ResponseEntity<>(
            bookingPaymentDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> createBookingPayment(@PathVariable int id, @RequestBody Payment payment) {
        BookingPayment bookingPayment = new BookingPayment();
        Booking booking = bookingRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found!"));

        bookingPayment.setBooking(booking);
        bookingPayment.setRefunded(payment.refunded);
        bookingPayment.setStripeId(payment.stripeId);

        try {
            return new ResponseEntity<>(bookingPaymentDB.save(bookingPayment), HttpStatus.CREATED);
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
    public ResponseEntity<?> updateBookingPayment(@PathVariable int id, @RequestBody Payment payment) throws ResponseStatusException {
        BookingPayment bookingPayment = bookingPaymentDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingPayment not found!")
        );

        bookingPayment.setRefunded(payment.refunded);
        bookingPayment.setStripeId(payment.stripeId);

        try {
            BookingPayment updatedBookingPayment = bookingPaymentDB.save(bookingPayment);
            return new ResponseEntity<>(updatedBookingPayment, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<?> deleteBookingPayment(@PathVariable int id) throws ResponseStatusException {
        BookingPayment bookingPayment = bookingPaymentDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookingPayment could not be found!"));

        try {
            bookingPaymentDB.delete(bookingPayment);
            return new ResponseEntity<>(bookingPayment, HttpStatus.NO_CONTENT);
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
