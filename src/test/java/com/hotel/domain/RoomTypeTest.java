package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.Currency;

class RoomTypeTest {

    @Test
    void testRoomTypeCreation_Success() {
        // Arrange
        RoomKind kind = RoomKind.DOUBLE;
        Money cost = new Money(new BigDecimal("150.00"), Currency.getInstance("USD"));

        // Act
        RoomType roomType = new RoomType(kind, cost);

        // Assert
        assertNotNull(roomType, "RoomType should be created successfully");
        assertEquals(kind, roomType.getKind(), "Kind should match provided value");
        assertEquals(cost, roomType.getCost(), "Cost should match provided value");
    }

    @ParameterizedTest
    @EnumSource(RoomKind.class)
    void testRoomTypeCreation_AllRoomKinds_Success(RoomKind kind) {
        // Arrange
        Money cost = new Money(new BigDecimal("100.00"), Currency.getInstance("USD"));

        // Act
        RoomType roomType = new RoomType(kind, cost);

        // Assert
        assertNotNull(roomType, "RoomType should be created for kind: " + kind);
        assertEquals(kind, roomType.getKind(), "Kind should match");
    }

    @Test
    void testRoomTypeCreation_NullKind_ThrowsException() {
        // Arrange
        RoomKind kind = null;
        Money cost = new Money(new BigDecimal("150.00"), Currency.getInstance("USD"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new RoomType(kind, cost),
                "Creating RoomType with null kind should throw IllegalArgumentException");
    }

    @Test
    void testRoomTypeCreation_NullCost_ThrowsException() {
        // Arrange
        RoomKind kind = RoomKind.DOUBLE;
        Money cost = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new RoomType(kind, cost),
                "Creating RoomType with null cost should throw IllegalArgumentException");
    }

    @Test
    void testRoomTypeEquality_SameValues() {
        // Arrange
        Money cost = new Money(new BigDecimal("150.00"), Currency.getInstance("USD"));
        RoomType roomType1 = new RoomType(RoomKind.DOUBLE, cost);
        RoomType roomType2 = new RoomType(RoomKind.DOUBLE, cost);

        // Act & Assert
        assertEquals(roomType1, roomType2, "RoomTypes with same values should be equal");
        assertEquals(roomType1.hashCode(), roomType2.hashCode(),
                "Equal RoomTypes should have same hashCode");
    }

    @Test
    void testRoomTypeEquality_DifferentKind() {
        // Arrange
        Money cost = new Money(new BigDecimal("150.00"), Currency.getInstance("USD"));
        RoomType roomType1 = new RoomType(RoomKind.DOUBLE, cost);
        RoomType roomType2 = new RoomType(RoomKind.SINGLE, cost);

        // Act & Assert
        assertNotEquals(roomType1, roomType2, "RoomTypes with different kinds should not be equal");
    }

    @Test
    void testRoomTypeEquality_DifferentCost() {
        // Arrange
        Money cost1 = new Money(new BigDecimal("150.00"), Currency.getInstance("USD"));
        Money cost2 = new Money(new BigDecimal("200.00"), Currency.getInstance("USD"));
        RoomType roomType1 = new RoomType(RoomKind.DOUBLE, cost1);
        RoomType roomType2 = new RoomType(RoomKind.DOUBLE, cost2);

        // Act & Assert
        assertNotEquals(roomType1, roomType2, "RoomTypes with different costs should not be equal");
    }

    @Test
    void testRoomTypeToString() {
        // Arrange
        Money cost = new Money(new BigDecimal("150.00"), Currency.getInstance("USD"));
        RoomType roomType = new RoomType(RoomKind.DOUBLE, cost);

        // Act
        String result = roomType.toString();

        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("DOUBLE"), "toString should contain room kind");
        assertTrue(result.contains("150"), "toString should contain cost amount");
    }
}
