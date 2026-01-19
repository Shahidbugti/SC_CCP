package com.hotel.domain;

import java.util.Objects;

/**
 * Represents a physical address with street, city, and postal code.
 */
public class Address {
    private final String street;
    private final String city;
    private final String zipCode;

    public Address(String street, String city, String zipCode) {
        validateStreet(street);
        validateCity(city);
        validateZipCode(zipCode);

        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    /**
     * Validates that street is provided and not empty.
     */
    private void validateStreet(String streetAddress) {
        if (streetAddress == null || isBlank(streetAddress)) {
            throw new IllegalArgumentException("Street address is required");
        }
    }

    /**
     * Validates that city is provided and not empty.
     */
    private void validateCity(String cityName) {
        if (cityName == null || isBlank(cityName)) {
            throw new IllegalArgumentException("City is required");
        }
    }

    /**
     * Validates that zip code is provided and not empty.
     */
    private void validateZipCode(String postalCode) {
        if (postalCode == null || isBlank(postalCode)) {
            throw new IllegalArgumentException("Zip code is required");
        }
    }

    /**
     * Checks if a string is blank (null or only whitespace).
     */
    private boolean isBlank(String text) {
        return text.trim().isEmpty();
    }

    /**
     * Checks if this address has all required fields.
     */
    public boolean hasCompleteAddress() {
        return street != null && !isBlank(street) &&
                city != null && !isBlank(city) &&
                zipCode != null && !isBlank(zipCode);
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Address otherAddress = (Address) other;
        return Objects.equals(street, otherAddress.street) &&
                Objects.equals(city, otherAddress.city) &&
                Objects.equals(zipCode, otherAddress.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, zipCode);
    }

    @Override
    public String toString() {
        return street + ", " + city + " " + zipCode;
    }
}
