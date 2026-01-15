package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class CreditCardTest {

    @Test
    void testCreditCardCreation_Success() {
        // Arrange
        String number = "1234567890123456";
        String expiryDate = "12/25";
        String cvv = "123";

        // Act
        CreditCard card = new CreditCard(number, expiryDate, cvv);

        // Assert
        assertNotNull(card, "CreditCard should be created successfully");
    }

    @ParameterizedTest
    @ValueSource(strings = { "1234567890123", "12345678901234", "123456789012345", "1234567890123456" })
    void testCreditCardCreation_ValidNumbers_Success(String validNumber) {
        // Arrange
        String expiryDate = "12/25";
        String cvv = "123";

        // Act
        CreditCard card = new CreditCard(validNumber, expiryDate, cvv);

        // Assert
        assertNotNull(card, "CreditCard should be created with valid number: " + validNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = { "123", "12345", "123456789012" })
    void testCreditCardCreation_InvalidNumbers_ThrowsException(String invalidNumber) {
        // Arrange
        String expiryDate = "12/25";
        String cvv = "123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(invalidNumber, expiryDate, cvv),
                "Creating CreditCard with number less than 13 digits should throw exception");
    }

    @Test
    void testCreditCardCreation_NullNumber_ThrowsException() {
        // Arrange
        String number = null;
        String expiryDate = "12/25";
        String cvv = "123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(number, expiryDate, cvv),
                "Creating CreditCard with null number should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  " })
    void testCreditCardCreation_InvalidExpiryDate_ThrowsException(String invalidExpiry) {
        // Arrange
        String number = "1234567890123456";
        String cvv = "123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(number, invalidExpiry, cvv),
                "Creating CreditCard with invalid expiry date should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @ValueSource(strings = { "12", "1" })
    void testCreditCardCreation_InvalidCVV_ThrowsException(String invalidCvv) {
        // Arrange
        String number = "1234567890123456";
        String expiryDate = "12/25";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(number, expiryDate, invalidCvv),
                "Creating CreditCard with CVV less than 3 characters should throw exception");
    }

    @Test
    void testCreditCardCreation_NullCVV_ThrowsException() {
        // Arrange
        String number = "1234567890123456";
        String expiryDate = "12/25";
        String cvv = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(number, expiryDate, cvv),
                "Creating CreditCard with null CVV should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @ValueSource(strings = { "123", "1234", "12345" })
    void testCreditCardCreation_ValidCVV_Success(String validCvv) {
        // Arrange
        String number = "1234567890123456";
        String expiryDate = "12/25";

        // Act
        CreditCard card = new CreditCard(number, expiryDate, validCvv);

        // Assert
        assertNotNull(card, "CreditCard should be created with valid CVV: " + validCvv);
    }

    @Test
    void testCreditCardEquality_SameNumber() {
        // Arrange
        CreditCard card1 = new CreditCard("1234567890123456", "12/25", "123");
        CreditCard card2 = new CreditCard("1234567890123456", "01/26", "456");

        // Act & Assert
        assertEquals(card1, card2, "CreditCards with same number should be equal");
        assertEquals(card1.hashCode(), card2.hashCode(),
                "Equal CreditCards should have same hashCode");
    }

    @Test
    void testCreditCardEquality_DifferentNumber() {
        // Arrange
        CreditCard card1 = new CreditCard("1234567890123456", "12/25", "123");
        CreditCard card2 = new CreditCard("9876543210987654", "12/25", "123");

        // Act & Assert
        assertNotEquals(card1, card2, "CreditCards with different numbers should not be equal");
    }

    @Test
    void testCreditCardToString_MasksNumber() {
        // Arrange
        CreditCard card = new CreditCard("1234567890123456", "12/25", "123");

        // Act
        String result = card.toString();

        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("3456"), "toString should contain last 4 digits");
        assertTrue(result.contains("XXXX"), "toString should mask the number");
        assertFalse(result.contains("1234567890123456"),
                "toString should not contain full card number");
    }
}
