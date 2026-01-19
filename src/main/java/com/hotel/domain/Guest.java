package com.hotel.domain;

/**
 * Represents a hotel guest with personal information.
 */
public class Guest {
    private final String name;
    private final Address addressDetails;
    private final Identity id;

    public Guest(String name, Address addressDetails, Identity id) {
        validateName(name);
        validateAddress(addressDetails);

        this.name = name;
        this.addressDetails = addressDetails;
        this.id = id;
    }

    /**
     * Validates that guest name is provided and not empty.
     */
    private void validateName(String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required");
        }
    }

    /**
     * Validates that address is provided.
     */
    private void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }
    }

    /**
     * Checks if this guest has identification on file.
     */
    public boolean hasIdentification() {
        return id != null;
    }

    public String getName() {
        return name;
    }

    public Address getAddressDetails() {
        return addressDetails;
    }

    public Identity getId() {
        return id;
    }
}
