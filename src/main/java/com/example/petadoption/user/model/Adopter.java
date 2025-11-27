package com.example.petadoption.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Adopter extends User {

    // Full Name (single field)
    @Column(nullable = false)
    private String fullName;

    // Phone Number
    @Column(nullable = false)
    private String phone;

    // Location / Address (one field from frontend)
    @Column(nullable = true)
    private String location;

    /* --------------------
       Getters & Setters
       -------------------- */

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
