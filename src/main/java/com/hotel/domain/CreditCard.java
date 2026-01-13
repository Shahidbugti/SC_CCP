package com.hotel.domain;

import java.util.Objects;

public class CreditCard {
    private final String number;
    private final String expiryDate;
    private final String cvv;

    public CreditCard(String number, String expiryDate, String cvv) {
        if (number == null || number.length() < 13) {
			throw new IllegalArgumentException("Invalid credit card number");
		}
        if (expiryDate == null || expiryDate.isBlank()) {
			throw new IllegalArgumentException("Expiry date cannot be empty");
		}
        if (cvv == null || cvv.length() < 3) {
			throw new IllegalArgumentException("Invalid CVV");
		}

        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        CreditCard that = (CreditCard) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "XXXX-XXXX-XXXX-" + number.substring(number.length() - 4);
    }
}
