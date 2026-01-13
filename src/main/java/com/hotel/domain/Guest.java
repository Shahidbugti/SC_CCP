package com.hotel.domain;

public class Guest {
    private final String name;
    private final Address addressDetails;
    private final Identity id;

    public Guest(String name, Address addressDetails, Identity id) {
        if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}
        if (addressDetails == null) {
			throw new IllegalArgumentException("Address cannot be null");
		}

        this.name = name;
        this.addressDetails = addressDetails;
        this.id = id;
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
