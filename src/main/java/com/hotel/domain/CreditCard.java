package com.hotel.domain;

import java.util.Objects;

/**
 * Represents a credit card with validation for secure payment processing.
 * Uses helper methods for clearer validation logic.
 */
public class CreditCard {
    private final String number;
    private final String expiryDate;
    private final String cvv;

    public CreditCard(String number, String expiryDate, String cvv) {
        // Validate each field using helper methods for better readability
        validateCardNumber(number);
        validateExpiryDate(expiryDate);
        validateCVV(cvv);

        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    /**
     * Validates that the card number is not null and has sufficient length.
     * Minimum 12 digits for international cards.
     */
    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || !isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Card number must be at least 13 digits");
        }
    }

    /**
     * Checks if card number meets minimum length requirement.
     */
    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.length() >= 13;
    }

    /**
     * Validates that expiry date is provided and not empty.
     */
    private void validateExpiryDate(String expiry) {
        if (expiry == null || expiry.trim().isEmpty()) {
            throw new IllegalArgumentException("Expiry date is required");
        }
    }

    /**
     * Validates that CVV is provided and has minimum 3 digits.
     */
    private void validateCVV(String securityCode) {
        if (securityCode == null || !isValidCVV(securityCode)) {
            throw new IllegalArgumentException("CVV must be at least 3 digits");
        }
    }

    /**
     * Checks if CVV meets minimum length requirement.
     */
    private boolean isValidCVV(String securityCode) {
        return securityCode.length() >= 3;
    }

    /**
     * Returns masked card number for display purposes.
     */
    public String getMaskedNumber() {
        return "XXXX-XXXX-XXXX-" + getLastFourDigits();
    }

    /**
     * Extracts last 4 digits of card number.
     */
    private String getLastFourDigits() {
        return number.substring(number.length() - 4);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        CreditCard otherCard = (CreditCard) other;
        return Objects.equals(number, otherCard.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return getMaskedNumber();
    }
}
