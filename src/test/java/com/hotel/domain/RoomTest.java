package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.hotel.exception.HotelException;
import java.math.BigDecimal;
import java.util.Currency;

class RoomTest {

    private Room room;
    private Guest guest;

    @BeforeEach
    void setUp() {
        RoomType type = new RoomType(RoomKind.SINGLE, new Money(10.0, "USD"));
        room = new Room(101, type);
        guest = new Guest("John", new Address("St", "City", "Zip"), new Identity("P", "1"));
    }

    @Test
    void testInitialState_ShouldBeFree() {
        // Assert
        assertTrue(room.isFree(), "Initial room state should be FREE (isFree() should be true)");
        assertEquals(RoomState.FREE, room.getState(), "Initial room state should be FREE");
    }

    @ParameterizedTest
    @EnumSource(RoomKind.class)
    void testRoomCreation_AllRoomKinds_Success(RoomKind kind) {
        // Arrange
        RoomType type = new RoomType(kind, new Money(100.0, "USD"));

        // Act
        Room newRoom = new Room(201, type);

        // Assert
        assertNotNull(newRoom, "Room should be created for kind: " + kind);
        assertEquals(kind, newRoom.getRoomType().getKind(), "Room kind should match");
        assertTrue(newRoom.isFree(), "New room should be in FREE state");
    }

    @Test
    void testBookRoom_WhenFree_TransitionsToReserved() {
        // Act
        room.bookRoom();

        // Assert
        assertTrue(room.isBooked(), "Room should be booked");
        assertEquals(RoomState.RESERVED, room.getState(), "Room state should transition to RESERVED");
    }

    @Test
    void testBookRoom_WhenAlreadyBooked_ThrowsException() {
        // Arrange
        room.bookRoom(); // State -> RESERVED

        // Act & Assert
        assertThrows(HotelException.class, () -> room.bookRoom(),
                "Booking an already booked room should throw HotelException");
    }

    @Test
    void testCheckInGuest_WhenReserved_TransitionsToOccupied() {
        // Arrange
        room.bookRoom();

        // Act
        room.checkInGuest(guest);

        // Assert
        assertTrue(room.isOccupied(), "Room should be occupied");
        assertEquals(RoomState.OCCUPIED, room.getState(), "Room state should transition to OCCUPIED");
        assertEquals(guest, room.getOccupant(), "Occupant should be the checked-in guest");
    }

    @Test
    void testCheckInGuest_WhenFree_ThrowsException() {
        // Act & Assert
        assertThrows(HotelException.class, () -> room.checkInGuest(guest),
                "Checking in guest to FREE room should throw HotelException");
    }

    @Test
    void testCheckInGuest_NullGuest_ThrowsException() {
        // Arrange
        room.bookRoom();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> room.checkInGuest(null),
                "Checking in null guest should throw IllegalArgumentException");
    }

    @Test
    void testCheckOutGuest_WhenOccupied_TransitionsToFree() {
        // Arrange
        room.bookRoom();
        room.checkInGuest(guest);

        // Act
        room.checkOutGuest();

        // Assert
        assertTrue(room.isFree(), "Room should be free after checkout");
        assertEquals(RoomState.FREE, room.getState(), "Room state should transition to FREE");
        assertNull(room.getOccupant(), "Occupant should be null after checkout");
    }

    @Test
    void testCheckOutGuest_WhenNotOccupied_ThrowsException() {
        // Act & Assert
        assertThrows(HotelException.class, () -> room.checkOutGuest(),
                "Checking out from non-occupied room should throw HotelException");
    }

    @Test
    void testCancelBooking_WhenReserved_TransitionsToFree() {
        // Arrange
        room.bookRoom();

        // Act
        room.cancelBooking();

        // Assert
        assertTrue(room.isFree(), "Room should be free after cancellation");
        assertEquals(RoomState.FREE, room.getState(), "Room state should transition to FREE");
    }

    @Test
    void testCancelBooking_WhenNotReserved_ThrowsException() {
        // Act & Assert
        assertThrows(HotelException.class, () -> room.cancelBooking(),
                "Cancelling booking on non-reserved room should throw HotelException");
    }

    @Test
    void testCompleteRoomLifecycle() {
        // Complete lifecycle
        assertTrue(room.isFree(), "Initial state should be FREE");

        room.bookRoom();
        assertTrue(room.isBooked(), "After booking should be RESERVED");

        room.checkInGuest(guest);
        assertTrue(room.isOccupied(), "After check-in should be OCCUPIED");

        room.checkOutGuest();
        assertTrue(room.isFree(), "After checkout should be FREE");
    }
}
