package com.ss.utopia.restapi.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class FlightBookingPK implements Serializable {
    private int flight;
    private int booking;

    public FlightBookingPK() {}

    public FlightBookingPK(int flight, int booking) {
        this.flight = flight;
        this.booking = booking;
    }

    public int getFlight() { return flight; };
    public int getBooking() { return booking; };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FlightBookingPK) {
            FlightBookingPK pk = (FlightBookingPK)obj;
            return flight == pk.flight && booking == pk.booking;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return flight + booking;
    }
}
