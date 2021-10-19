package com.ss.utopia.restapi.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "airport")
public class Airport {
    @Id
    @Column(name = "iata_id", nullable = false, unique = true)
    private String iataID;

    @Column(name = "city", nullable = false)
    private String cityName;

    public String getIataID() { return iataID; }
    public void setIataID(String iataID) { this.iataID = iataID; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
}
