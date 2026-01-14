package com.hotel.domain;

import java.util.Objects;

public class Identity {
    private final String idNumber;
    private final String type; // e.g., Passport, Driving License

    public Identity(String type, String idNumber) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Identity type cannot be empty");
        }
        if (idNumber == null || idNumber.isBlank()) {
            throw new IllegalArgumentException("ID number cannot be empty");
        }

        this.type = type;
        this.idNumber = idNumber;
    }

    public String getType() {
        return type;
    }

    public String getIdNumber() {
        return idNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Identity identity = (Identity) o;
        return Objects.equals(idNumber, identity.idNumber) &&
                Objects.equals(type, identity.type);
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
