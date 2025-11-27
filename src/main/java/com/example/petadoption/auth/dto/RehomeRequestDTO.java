package com.example.petadoption.auth.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO used internally in service layer for processing
 * the “Rehome a Pet” submission. This is populated by
 * RehomeRequestForm.toDto().
 *
 * Contains:
 *  - Owner details
 *  - Pet details
 *  - Uploaded photo files
 *  - Agreements
 */
public class RehomeRequestDTO {

    /* ----------------------
       Owner Information
       ---------------------- */
    private String fullName;
    private String email;
    private String phone;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;

    /* ----------------------
       Pet Information
       ---------------------- */
    private String petName;
    private String species;
    private String breed;
    private Integer age;
    private String size;
    private String gender;

    private Double adoptionFee;

    private String description;
    private String reasonForRehoming;

    private Boolean spayed;
    private Boolean vaccinated;

    /* ----------------------
       File Uploads
       ---------------------- */
    private MultipartFile[] petPhotos;

    /* ----------------------
       Agreements (Step 3)
       ---------------------- */
    private Boolean agreeTerms;
    private Boolean agreeScreening;


    /* ----------------------
       Getters & Setters
       ---------------------- */

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetAddress() {
        return streetAddress;
    }
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPetName() {
        return petName;
    }
    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getAdoptionFee() {
        return adoptionFee;
    }
    public void setAdoptionFee(Double adoptionFee) {
        this.adoptionFee = adoptionFee;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getReasonForRehoming() {
        return reasonForRehoming;
    }
    public void setReasonForRehoming(String reasonForRehoming) {
        this.reasonForRehoming = reasonForRehoming;
    }

    public Boolean getSpayed() {
        return spayed;
    }
    public void setSpayed(Boolean spayed) {
        this.spayed = spayed;
    }

    public Boolean getVaccinated() {
        return vaccinated;
    }
    public void setVaccinated(Boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public MultipartFile[] getPetPhotos() {
        return petPhotos;
    }
    public void setPetPhotos(MultipartFile[] petPhotos) {
        this.petPhotos = petPhotos;
    }

    public Boolean getAgreeTerms() {
        return agreeTerms;
    }
    public void setAgreeTerms(Boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    public Boolean getAgreeScreening() {
        return agreeScreening;
    }
    public void setAgreeScreening(Boolean agreeScreening) {
        this.agreeScreening = agreeScreening;
    }
}
