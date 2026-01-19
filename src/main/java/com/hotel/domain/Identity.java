package com.hotel.domain;

import java.util.Objects;

/**
 * Represents a person's identification document.
 * Examples: Passport, Driver's License, National ID
 */
public class Identity {
    private final String idNumber;
    private final String type; // e.g., Passport, Driving License

    public Identity(String type, String idNumber) {
        validateType(type);
        validateIdNumber(idNumber);

        this.type = type;
        this.idNumber = idNumber;
    }

    /**
     * Validates that identity type is provided and not empty.
     */
    private void validateType(String idType) {
        if (idType == null || isEmpty(idType)) {
            throw new IllegalArgumentException("Identity type is required");
        }
    }

    /**
     * Validates that ID number is provided and not empty.
     */
    private void validateIdNumber(String number) {
        if (number == null || isEmpty(number)) {
            throw new IllegalArgumentException("ID number is required");
        }
    }

    /**
     * Checks if a string is empty or contains only whitespace.
     */
    private boolean isEmpty(String text) {
        return text.trim().isEmpty();
    }

    /**
     * Checks if this identity has valid information.
     */
    public boolean isValid() {
        return type != null && !isEmpty(type) &&
                idNumber != null && !isEmpty(idNumber);
    }

    public String getType() {
        return type;
    }

    public String getIdNumber() {
        return idNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Identity otherIdentity = (Identity) other;
        return Objects.equals(idNumber, otherIdentity.idNumber) &&
                Objects.equals(type, otherIdentity.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber, type);
    }

    @Override
    public String toString() {
        return type + ": " + idNumber;
    }
}
