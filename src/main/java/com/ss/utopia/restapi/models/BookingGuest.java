package com.ss.utopia.restapi.models;

import javax.persistence.*;

@Entity(name = "booking_guest")
public class BookingGuest {
    @Id
    @Column(name = "booking_id", nullable = false)
    private int bookingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(name = "contact_email", nullable = false)
    private String email;

    @Column(name = "contact_phone", nullable = false)
    private String phone;

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String contactEmail) { this.email = contactEmail; }
}
