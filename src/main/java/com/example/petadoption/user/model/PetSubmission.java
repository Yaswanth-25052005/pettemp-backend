package com.example.petadoption.user.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing a pet rehome submission (Owner + Pet + Photos + metadata).
 *
 * This class stores owner contact info, pet details, uploaded photo URLs, and submission metadata.
 */
@Entity
@Table(name = "pet_submissions")
public class PetSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ----------------------
       Owner information
       ---------------------- */
    @Column(name = "owner_full_name")
    private String ownerFullName;

    @Column(name = "owner_email")
    private String ownerEmail;

    @Column(name = "owner_phone")
    private String ownerPhone;

    @Column(name = "owner_street_address")
    private String ownerStreetAddress;

    @Column(name = "owner_city")
    private String ownerCity;

    @Column(name = "owner_state")
    private String ownerState;

    @Column(name = "owner_zip_code")
    private String ownerZipCode;

    /* ----------------------
       Pet information
       ---------------------- */
    @Column(name = "pet_name")
    private String petName;

    @Column(name = "species")
    private String species;

    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "size")
    private String size; // small, medium, large, etc.

    @Column(name = "gender")
    private String gender;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "reason_for_rehoming", columnDefinition = "TEXT")
    private String reasonForRehoming;

    @Column(name = "spayed")
    private Boolean spayed = false;

    @Column(name = "vaccinated")
    private Boolean vaccinated = false;

    @Column(name = "adoption_fee")
    private Double adoptionFee = 0.0;

    /* ----------------------
       Photo URLs (stored as simple collection of strings)
       ---------------------- */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pet_submission_photos", joinColumns = @JoinColumn(name = "pet_submission_id"))
    @Column(name = "photo_url", length = 1024)
    private List<String> photoUrls = new ArrayList<>();

    /* ----------------------
       Agreements & Metadata
       ---------------------- */
    @Column(name = "agree_terms")
    private Boolean agreeTerms = false;

    @Column(name = "agree_screening")
    private Boolean agreeScreening = false;

    @Column(name = "status")
    private String status = "PENDING"; // e.g., PENDING, APPROVED, REJECTED

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ----------------------
       Constructors
       ---------------------- */
    public PetSubmission() {
    }

    // Basic constructor (convenience)
    public PetSubmission(String ownerFullName, String ownerEmail, String ownerPhone,
                         String ownerStreetAddress, String ownerCity, String ownerState, String ownerZipCode,
                         String petName, String species, String breed, Integer age, String size, String gender,
                         String description, String reasonForRehoming, Boolean spayed, Boolean vaccinated,
                         Double adoptionFee, List<String> photoUrls, Boolean agreeTerms, Boolean agreeScreening) {
        this.ownerFullName = ownerFullName;
        this.ownerEmail = ownerEmail;
        this.ownerPhone = ownerPhone;
        this.ownerStreetAddress = ownerStreetAddress;
        this.ownerCity = ownerCity;
        this.ownerState = ownerState;
        this.ownerZipCode = ownerZipCode;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.size = size;
        this.gender = gender;
        this.description = description;
        this.reasonForRehoming = reasonForRehoming;
        this.spayed = spayed != null ? spayed : false;
        this.vaccinated = vaccinated != null ? vaccinated : false;
        this.adoptionFee = adoptionFee != null ? adoptionFee : 0.0;
        if (photoUrls != null) {
            this.photoUrls = photoUrls;
        }
        this.agreeTerms = agreeTerms != null ? agreeTerms : false;
        this.agreeScreening = agreeScreening != null ? agreeScreening : false;
    }

    /* ----------------------
       Getters & Setters
       ---------------------- */

    public Long getId() {
        return id;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerStreetAddress() {
        return ownerStreetAddress;
    }

    public void setOwnerStreetAddress(String ownerStreetAddress) {
        this.ownerStreetAddress = ownerStreetAddress;
    }

    public String getOwnerCity() {
        return ownerCity;
    }

    public void setOwnerCity(String ownerCity) {
        this.ownerCity = ownerCity;
    }

    public String getOwnerState() {
        return ownerState;
    }

    public void setOwnerState(String ownerState) {
        this.ownerState = ownerState;
    }

    public String getOwnerZipCode() {
        return ownerZipCode;
    }

    public void setOwnerZipCode(String ownerZipCode) {
        this.ownerZipCode = ownerZipCode;
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

    public Double getAdoptionFee() {
        return adoptionFee;
    }

    public void setAdoptionFee(Double adoptionFee) {
        this.adoptionFee = adoptionFee;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /* ----------------------
       equals / hashCode / toString
       ---------------------- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetSubmission that = (PetSubmission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "PetSubmission{" +
                "id=" + id +
                ", ownerFullName='" + ownerFullName + '\'' +
                ", ownerEmail='" + ownerEmail + '\'' +
                ", petName='" + petName + '\'' +
                ", species='" + species + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
