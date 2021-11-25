package com.ss.utopia.restapi.models;

import java.io.Serializable;

import javax.persistence.*;

import jdk.jfr.Unsigned;

@Entity(name = "booking")
public class Booking implements Serializable {
    @Id()
    @Unsigned
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    public Booking() {};

    public Booking(Integer id, Boolean isActive, String code) {
        this.id = id;
        this.isActive = isActive;
        this.confirmationCode = code;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getConfirmationCode() { return confirmationCode; }
    public void setConfirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; }
}
