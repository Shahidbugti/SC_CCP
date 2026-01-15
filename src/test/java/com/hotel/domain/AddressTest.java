package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AddressTest {

    @Test
    void testAddressCreation_Success() {
        // Arrange
        String street = "123 Main Street";
        String city = "New York";
        String zipCode = "10001";

        // Act
        Address address = new Address(street, city, zipCode);

        // Assert
        assertNotNull(address, "Address should be created successfully");
        assertEquals(street + ", " + city + " " + zipCode, address.toString(),
                "toString should format address correctly");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void testAddressCreation_InvalidStreet_ThrowsException(String invalidStreet) {
        // Arrange
        String city = "New York";
        String zipCode = "10001";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Address(invalidStreet, city, zipCode),
                "Creating Address with invalid street should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void testAddressCreation_InvalidCity_ThrowsException(String invalidCity) {
        // Arrange
        String street = "123 Main Street";
        String zipCode = "10001";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Address(street, invalidCity, zipCode),
                "Creating Address with invalid city should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void testAddressCreation_InvalidZipCode_ThrowsException(String invalidZipCode) {
        // Arrange
        String street = "123 Main Street";
        String city = "New York";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Address(street, city, invalidZipCode),
                "Creating Address with invalid zipCode should throw IllegalArgumentException");
    }

    @Test
    void testAddressEquality_SameValues() {
        // Arrange
        Address address1 = new Address("123 Main St", "NYC", "10001");
        Address address2 = new Address("123 Main St", "NYC", "10001");

        // Act & Assert
        assertEquals(address1, address2, "Addresses with same values should be equal");
        assertEquals(address1.hashCode(), address2.hashCode(),
                "Equal addresses should have same hashCode");
    }

    @Test
    void testAddressEquality_DifferentStreet() {
        // Arrange
        Address address1 = new Address("123 Main St", "NYC", "10001");
        Address address2 = new Address("456 Oak Ave", "NYC", "10001");

        // Act & Assert
        assertNotEquals(address1, address2, "Addresses with different streets should not be equal");
    }

    @Test
    void testAddressEquality_DifferentCity() {
        // Arrange
        Address address1 = new Address("123 Main St", "NYC", "10001");
        Address address2 = new Address("123 Main St", "Boston", "10001");

        // Act & Assert
        assertNotEquals(address1, address2, "Addresses with different cities should not be equal");
    }

    @Test
    void testAddressEquality_DifferentZipCode() {
        // Arrange
        Address address1 = new Address("123 Main St", "NYC", "10001");
        Address address2 = new Address("123 Main St", "NYC", "10002");

        // Act & Assert
        assertNotEquals(address1, address2, "Addresses with different zip codes should not be equal");
    }

    @Test
    void testAddressToString() {
        // Arrange
        Address address = new Address("123 Main St", "NYC", "10001");

        // Act
        String result = address.toString();

        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("123 Main St"), "toString should contain street");
        assertTrue(result.contains("NYC"), "toString should contain city");
        assertTrue(result.contains("10001"), "toString should contain zip code");
    }
}
