package com.ss.utopia.restapi.models;

import javax.persistence.*;

@Entity(name = "booking_user")
@IdClass(BookingUserPK.class)
public class BookingUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BookingUser() {}
    public BookingUser(Booking booking, User user) {
        this.booking = booking;
        this.user = user;
    }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
