package com.ss.utopia.restapi.controllers;

import com.ss.utopia.restapi.dao.PassengerRepository;
import com.ss.utopia.restapi.models.Passenger;
import com.ss.utopia.restapi.services.ResetAutoCounterService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/passengers")
public class PassengerController {

    @Autowired
    PassengerRepository passengerDB;

    @Autowired
    ResetAutoCounterService resetService;

    @GetMapping(path="/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable int id) throws ResponseStatusException {
        return new ResponseEntity<>(passengerDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger not found!")),
            HttpStatus.OK
        );
    }

    @GetMapping(path={"/all", ""})
    public ResponseEntity<Iterable<Passenger>> getAllPassengers() {
        return new ResponseEntity<>(
            passengerDB.findAll(),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createPassenger(@RequestBody Passenger passenger) {
        try {
            resetService.resetAutoCounter("passenger");
            return new ResponseEntity<>(passengerDB.save(passenger), HttpStatus.CREATED);
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

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updatePassenger(@PathVariable int id, @RequestBody Passenger passengerDetails)
            throws ResponseStatusException {
        Passenger passenger = passengerDB.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger not found!"));

        passenger.setAddress(passengerDetails.getAddress());
        passenger.setGender(passengerDetails.getGender());
        passenger.setDateOfBirth(passengerDetails.getDateOfBirth());
        passenger.setFamilyName(passengerDetails.getFamilyName());
        passenger.setGivenName(passengerDetails.getGivenName());
        passenger.setBooking(passengerDetails.getBooking());

        try {
            Passenger updatedPassenger = passengerDB.save(passenger);
            return new ResponseEntity<>(updatedPassenger, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<?> deletePassenger(@PathVariable int id) throws ResponseStatusException {
        Passenger passenger = passengerDB
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger could not be found!"));

        try {
            passengerDB.delete(passenger);
            resetService.resetAutoCounter("passenger");
            return new ResponseEntity<>(passenger, HttpStatus.NO_CONTENT);
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