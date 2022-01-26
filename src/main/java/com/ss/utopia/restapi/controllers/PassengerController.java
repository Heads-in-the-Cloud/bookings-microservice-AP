package com.ss.utopia.restapi.controllers;

import java.sql.Date;

import com.ss.utopia.restapi.dao.PassengerRepository;
import com.ss.utopia.restapi.models.Passenger;
import com.ss.utopia.restapi.services.ResetAutoCounterService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

class PassengerPK {
    public int booking;
    public String givenName;
    public String familyName;
    public Date dateOfBirth;
    public String gender;
    public String address;
}

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

        if (passengerDetails.getAddress() != null) passenger.setAddress(passengerDetails.getAddress());
        if (passengerDetails.getGender() != null) passenger.setGender(passengerDetails.getGender());
        if (passengerDetails.getDateOfBirth() != null) passenger.setDateOfBirth(passengerDetails.getDateOfBirth());
        if (passengerDetails.getFamilyName() != null) passenger.setFamilyName(passengerDetails.getFamilyName());
        if (passengerDetails.getGivenName() != null) passenger.setGivenName(passengerDetails.getGivenName());
        if (passengerDetails.getBooking() != null) passenger.setBooking(passengerDetails.getBooking());

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
