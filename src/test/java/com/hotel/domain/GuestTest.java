package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class GuestTest {

    @Test
    void testGuestCreation_Success() {
        // Arrange
        String name = "John Doe";
        Address address = new Address("123 Main St", "NYC", "10001");
        Identity identity = new Identity("Passport", "ABC123");

        // Act
        Guest guest = new Guest(name, address, identity);

        // Assert
        assertNotNull(guest, "Guest should be created successfully");
        assertEquals(name, guest.getName(), "Name should match provided value");
        assertEquals(address, guest.getAddressDetails(), "Address should match provided value");
        assertEquals(identity, guest.getId(), "Identity should match provided value");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void testGuestCreation_InvalidName_ThrowsException(String invalidName) {
        // Arrange
        Address address = new Address("123 Main St", "NYC", "10001");
        Identity identity = new Identity("Passport", "ABC123");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Guest(invalidName, address, identity),
                "Creating Guest with invalid name should throw IllegalArgumentException");
    }

    @Test
    void testGuestCreation_NullAddress_ThrowsException() {
        // Arrange
        String name = "John Doe";
        Address address = null;
        Identity identity = new Identity("Passport", "ABC123");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Guest(name, address, identity),
                "Creating Guest with null address should throw IllegalArgumentException");
    }

    @Test
    void testGuestCreation_NullIdentity_Success() {
        // Arrange
        String name = "John Doe";
        Address address = new Address("123 Main St", "NYC", "10001");
        Identity identity = null;

        // Act
        Guest guest = new Guest(name, address, identity);

        // Assert
        assertNotNull(guest, "Guest should be created even with null identity");
        assertNull(guest.getId(), "Identity should be null");
    }

    @ParameterizedTest
    @ValueSource(strings = { "John Doe", "Jane Smith", "Bob Johnson", "Alice Williams" })
    void testGuestCreation_VariousNames_Success(String name) {
        // Arrange
        Address address = new Address("123 Main St", "NYC", "10001");
        Identity identity = new Identity("Passport", "ABC123");

        // Act
        Guest guest = new Guest(name, address, identity);

        // Assert
        assertNotNull(guest, "Guest should be created with name: " + name);
        assertEquals(name, guest.getName(), "Name should match");
    }

    @Test
    void testGuestGetters() {
        // Arrange
        String name = "John Doe";
        Address address = new Address("123 Main St", "NYC", "10001");
        Identity identity = new Identity("Passport", "ABC123");
        Guest guest = new Guest(name, address, identity);

        // Act
        String retrievedName = guest.getName();
        Address retrievedAddress = guest.getAddressDetails();
        Identity retrievedIdentity = guest.getId();

        // Assert
        assertEquals(name, retrievedName, "getName should return correct name");
        assertEquals(address, retrievedAddress, "getAddressDetails should return correct address");
        assertEquals(identity, retrievedIdentity, "getId should return correct identity");
    }
}
