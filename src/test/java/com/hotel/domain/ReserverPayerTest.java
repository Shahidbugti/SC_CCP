package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

class ReserverPayerTest {

    @Test
    void testReserverPayerCreation_Success() {
        // Arrange
        Identity identity = new Identity("Passport", "ABC123");
        CreditCard creditCard = new CreditCard("1234567890123456", "12/25", "123");

        // Act
        ReserverPayer payer = new ReserverPayer(identity, creditCard);

        // Assert
        assertNotNull(payer, "ReserverPayer should be created successfully");
        assertEquals(identity, payer.getId(), "Identity should match provided value");
        assertEquals(creditCard, payer.getCreditCardDetails(), "CreditCard should match provided value");
    }

    @Test
    void testReserverPayerCreation_NullIdentity_ThrowsException() {
        // Arrange
        Identity identity = null;
        CreditCard creditCard = new CreditCard("1234567890123456", "12/25", "123");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ReserverPayer(identity, creditCard),
                "Creating ReserverPayer with null identity should throw IllegalArgumentException");
    }

    @Test
    void testReserverPayerCreation_NullCreditCard_ThrowsException() {
        // Arrange
        Identity identity = new Identity("Passport", "ABC123");
        CreditCard creditCard = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ReserverPayer(identity, creditCard),
                "Creating ReserverPayer with null credit card should throw IllegalArgumentException");
    }

    @Test
    void testReserverPayerGetters() {
        // Arrange
        Identity identity = new Identity("Passport", "ABC123");
        CreditCard creditCard = new CreditCard("1234567890123456", "12/25", "123");
        ReserverPayer payer = new ReserverPayer(identity, creditCard);

        // Act
        Identity retrievedIdentity = payer.getId();
        CreditCard retrievedCard = payer.getCreditCardDetails();

        // Assert
        assertEquals(identity, retrievedIdentity, "getId should return correct identity");
        assertEquals(creditCard, retrievedCard, "getCreditCardDetails should return correct card");
    }
}
