package com.ss.utopia.restapi.models;

import java.sql.Timestamp;

import javax.persistence.*;

import jdk.jfr.Unsigned;

@Entity(name = "flight")
public class Flight {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @Unsigned
    private int id;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Column(name = "departure_time", nullable = false)
    private Timestamp departureTime;

    @Column(name = "reserved_seats", nullable = false)
    @Unsigned
    private int reservedSeats;

    @Column(name = "seat_price", nullable = false)
    private float seatPrice;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public float getSeatPrice() { return seatPrice; }
    public void setSeatPrice(float seatPrice) { this.seatPrice = seatPrice; }

    public int getReservedSeats() { return reservedSeats; }
    public void setReservedSeats(int reservedSeats) { this.reservedSeats = reservedSeats; }

    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp departureTime) { this.departureTime = departureTime; }

    public Airplane getAirplane() { return airplane; }
    public void setAirplane(Airplane airplane) { this.airplane = airplane; }

    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
}
