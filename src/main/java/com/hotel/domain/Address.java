package com.hotel.domain;

import java.util.Objects;

public class Address {
    private final String street;
    private final String city;
    private final String zipCode;

    public Address(String street, String city, String zipCode) {
        if (street == null || street.isBlank()) {
			throw new IllegalArgumentException("Street cannot be empty");
		}
        if (city == null || city.isBlank()) {
			throw new IllegalArgumentException("City cannot be empty");
		}
        if (zipCode == null || zipCode.isBlank()) {
			throw new IllegalArgumentException("ZipCode cannot be empty");
		}

        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
               Objects.equals(city, address.city) &&
               Objects.equals(zipCode, address.zipCode);
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
