package com.ss.utopia.restapi.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import jdk.jfr.Unsigned;

@Entity(name = "airplane_type")
public class AirplaneType {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @Unsigned
    private Integer id;

    @Column(name = "max_capacity", nullable = false, unique = true)
    @Unsigned
    private int maxCapacity;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    @Override
    public String toString() {
        return "max capacity: " + getMaxCapacity();
    }
}
