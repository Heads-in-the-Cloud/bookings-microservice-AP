package com.ss.utopia.restapi.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class BookingAgentPK implements Serializable {
    private int booking;
    private int agent;

    public BookingAgentPK() {}

    public BookingAgentPK(int booking, int user) {
        this.booking = booking;
        this.agent = user;
    }

    public int getBooking() { return booking; };
    public int getAgent() { return agent; };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookingAgentPK) {
            BookingAgentPK pk = (BookingAgentPK)obj;
            return agent == pk.agent && booking == pk.booking;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return agent + booking;
    }
}
