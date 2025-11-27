package com.example.petadoption.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Shelter extends User {

    // Organization Name
    @Column(nullable = false)
    private String organizationName;

    // Phone Number
    @Column(nullable = false)
    private String phone;

    // Single Location field (city + state + address combined)
    @Column(nullable = true)
    private String location;

    /* --------------------
       Getters & Setters
       -------------------- */

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
