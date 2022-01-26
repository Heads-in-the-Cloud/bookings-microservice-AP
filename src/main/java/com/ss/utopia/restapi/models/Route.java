package com.ss.utopia.restapi.models;

import java.io.Serializable;

import javax.persistence.*;

@Entity(name = "route")
public class Route implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="origin_id", nullable = false)
    private Airport origin;

    @ManyToOne
    @JoinColumn(name="destination_id", nullable = false)
    private Airport destination;

    public Route() {}
    public Route(Integer id, Airport origin, Airport destination) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Airport getOrigin() { return origin; }
    public void setOrigin(Airport origin) { this.origin = origin; }

    public Airport getDestination() { return destination; }
    public void setDestination(Airport destination) { this.destination = destination; }

    @Override
    public String toString() {
        return getOrigin().toString() + " -> " + getDestination().toString();
    }
}
