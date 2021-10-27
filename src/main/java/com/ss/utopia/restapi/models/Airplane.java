package com.ss.utopia.restapi.models;

import javax.persistence.*;

import jdk.jfr.Unsigned;

@Entity(name = "airplane")
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @Unsigned
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private AirplaneType airplaneType;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public AirplaneType getAirplaneType() { return airplaneType; }
    public void setAirplaneType(AirplaneType airplaneType) { this.airplaneType = airplaneType; }

    @Override
    public String toString() { return getId() + ", " + getAirplaneType().toString(); }
}
