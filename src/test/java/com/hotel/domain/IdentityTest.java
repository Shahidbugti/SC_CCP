package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

class IdentityTest {

    @Test
    void testIdentityCreation_Success() {
        // Arrange
        String type = "Passport";
        String idNumber = "ABC123456";

        // Act
        Identity identity = new Identity(type, idNumber);

        // Assert
        assertNotNull(identity, "Identity should be created successfully");
        assertEquals(type, identity.getType(), "Type should match provided value");
        assertEquals(idNumber, identity.getIdNumber(), "ID number should match provided value");
    }

    @ParameterizedTest
    @CsvSource({
            "Passport, ABC123456",
            "Driver License, DL-987654",
            "National ID, NID-111222",
            "SSN, 123-45-6789"
    })
    void testIdentityCreation_VariousTypes_Success(String type, String idNumber) {
        // Arrange & Act
        Identity identity = new Identity(type, idNumber);

        // Assert
        assertNotNull(identity, "Identity should be created for type: " + type);
        assertEquals(type, identity.getType(), "Type should match");
        assertEquals(idNumber, identity.getIdNumber(), "ID number should match");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void testIdentityCreation_InvalidType_ThrowsException(String invalidType) {
        // Arrange
        String idNumber = "ABC123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Identity(invalidType, idNumber),
                "Creating Identity with invalid type should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void testIdentityCreation_InvalidIdNumber_ThrowsException(String invalidIdNumber) {
        // Arrange
        String type = "Passport";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Identity(type, invalidIdNumber),
                "Creating Identity with invalid ID number should throw IllegalArgumentException");
    }

    @Test
    void testIdentityEquality_SameValues() {
        // Arrange
        Identity identity1 = new Identity("Passport", "ABC123");
        Identity identity2 = new Identity("Passport", "ABC123");

        // Act & Assert
        assertEquals(identity1, identity2, "Identities with same values should be equal");
        assertEquals(identity1.hashCode(), identity2.hashCode(),
                "Equal identities should have same hashCode");
    }

    @Test
    void testIdentityEquality_DifferentType() {
        // Arrange
        Identity identity1 = new Identity("Passport", "ABC123");
        Identity identity2 = new Identity("Driver License", "ABC123");

        // Act & Assert
        assertNotEquals(identity1, identity2, "Identities with different types should not be equal");
    }

    @Test
    void testIdentityEquality_DifferentIdNumber() {
        // Arrange
        Identity identity1 = new Identity("Passport", "ABC123");
        Identity identity2 = new Identity("Passport", "XYZ789");

        // Act & Assert
        assertNotEquals(identity1, identity2, "Identities with different ID numbers should not be equal");
    }

    @Test
    void testIdentityToString() {
        // Arrange
        Identity identity = new Identity("Passport", "ABC123456");

        // Act
        String result = identity.toString();

        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("Passport"), "toString should contain type");
        assertTrue(result.contains("ABC123456"), "toString should contain ID number");
        assertEquals("Passport: ABC123456", result, "toString should follow expected format");
    }
}
