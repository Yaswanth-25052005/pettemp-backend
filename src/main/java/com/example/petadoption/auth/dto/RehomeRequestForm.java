package com.example.petadoption.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

/**
 * Form object used for binding multipart/form-data from the
 * "Rehome a Pet" frontend. Designed for use with:
 *
 *   @PostMapping(value = "/rehome", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 *   public ResponseEntity<?> submitPetRehome(@ModelAttribute RehomeRequestForm form)
 *
 * Validation annotations are Jakarta (Spring Boot 3+).
 */
public class RehomeRequestForm {

    /* ----------------------
       Owner Information
       ---------------------- */
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;

    /* ----------------------
       Pet Information
       ---------------------- */
    @NotBlank(message = "Pet name is required")
    private String petName;

    @NotBlank(message = "Species is required")
    private String species;

    private String breed;

    // allow null for unknown age; validation optional
    private Integer age;

    private String size;
    private String gender;

    @PositiveOrZero(message = "Adoption fee must be zero or positive")
    private Double adoptionFee;

    private String description;
    private String reasonForRehoming;

    private Boolean spayed;
    private Boolean vaccinated;

    /* ----------------------
       File Upload(s)
       ---------------------- */
    // Leave file validation minimal to avoid binding errors
    private MultipartFile[] petPhotos;

    /* ----------------------
       Agreements (Step 3)
       ---------------------- */
    @NotNull(message = "You must agree to terms")
    @AssertTrue(message = "You must agree to the terms")
    private Boolean agreeTerms;

    @NotNull(message = "You must agree to screening")
    @AssertTrue(message = "You must agree to the screening")
    private Boolean agreeScreening;

    /* ----------------------
       Getters / Setters
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

    /* ----------------------
       Helper: convert to RehomeRequestDTO (files are kept separately in form)
       ---------------------- */
    public RehomeRequestDTO toDto() {
        RehomeRequestDTO dto = new RehomeRequestDTO();

        dto.setFullName(this.fullName);
        dto.setEmail(this.email);
        dto.setPhone(this.phone);
        dto.setStreetAddress(this.streetAddress);
        dto.setCity(this.city);
        dto.setState(this.state);
        dto.setZipCode(this.zipCode);

        dto.setPetName(this.petName);
        dto.setSpecies(this.species);
        dto.setBreed(this.breed);
        dto.setAge(this.age);
        dto.setSize(this.size);
        dto.setGender(this.gender);

        dto.setAdoptionFee(this.adoptionFee);
        dto.setDescription(this.description);
        dto.setReasonForRehoming(this.reasonForRehoming);

        dto.setSpayed(this.spayed);
        dto.setVaccinated(this.vaccinated);

        // NOTE: DTO also exposes MultipartFile[] in your provided DTO;
        // if you want to copy files into DTO as well, uncomment below:
        dto.setPetPhotos(this.petPhotos);

        dto.setAgreeTerms(this.agreeTerms);
        dto.setAgreeScreening(this.agreeScreening);

        return dto;
    }
}
