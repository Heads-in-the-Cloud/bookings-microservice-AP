package com.ss.utopia.restapi.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class BookingUserPK implements Serializable {
    private int booking;
    private int user;

    public BookingUserPK() {}

    public BookingUserPK(int booking, int user) {
        this.booking = booking;
        this.user = user;
    }

    public int getBooking() { return booking; };
    public int getUser() { return user; };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookingUserPK) {
            BookingUserPK pk = (BookingUserPK)obj;
            return user == pk.user && booking == pk.booking;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return user + booking;
    }
}
