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
        RoomType type = new RoomType(RoomKind.SINGLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        room = new Room(101, type);
        guest = new Guest("John", new Address("St", "City", "Zip"), new Identity("P", "1"));
    }

    @Test
    void testInitialState_ShouldBeFree() {
        // Arrange
        // (Setup done in @BeforeEach)

        // Act
        RoomState state = room.getState();

        // Assert
        assertEquals(RoomState.FREE, state, "Initial room state should be FREE");
    }

    @ParameterizedTest
    @EnumSource(RoomKind.class)
    void testRoomCreation_AllRoomKinds_Success(RoomKind kind) {
        // Arrange
        RoomType type = new RoomType(kind, new Money(BigDecimal.valueOf(100), Currency.getInstance("USD")));

        // Act
        Room newRoom = new Room(201, type);

        // Assert
        assertNotNull(newRoom, "Room should be created for kind: " + kind);
        assertEquals(kind, newRoom.getRoomType().getKind(), "Room kind should match");
        assertEquals(RoomState.FREE, newRoom.getState(), "New room should be in FREE state");
    }

    @Test
    void testMakeReservation_WhenFree_TransitionsToReserved() {
        // Arrange
        // (Room is FREE from @BeforeEach)

        // Act
        room.makeReservation();

        // Assert
        assertEquals(RoomState.RESERVED, room.getState(), "Room state should transition to RESERVED");
    }

    @Test
    void testMakeReservation_WhenAlreadyReserved_ThrowsException() {
        // Arrange
        room.makeReservation(); // State -> RESERVED

        // Act & Assert
        assertThrows(HotelException.class, () -> room.makeReservation(),
                "Making reservation on already reserved room should throw HotelException");
    }

    @Test
    void testCheckInGuest_WhenReserved_TransitionsToOccupied() {
        // Arrange
        room.makeReservation();

        // Act
        room.checkInGuest(guest);

        // Assert
        assertEquals(RoomState.OCCUPIED, room.getState(), "Room state should transition to OCCUPIED");
        assertEquals(guest, room.getOccupant(), "Occupant should be the checked-in guest");
    }

    @Test
    void testCheckInGuest_WhenFree_ThrowsException() {
        // Arrange
        // Room is FREE

        // Act & Assert
        assertThrows(HotelException.class, () -> room.checkInGuest(guest),
                "Checking in guest to FREE room should throw HotelException");
    }

    @Test
    void testCheckInGuest_NullGuest_ThrowsException() {
        // Arrange
        room.makeReservation();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> room.checkInGuest(null),
                "Checking in null guest should throw IllegalArgumentException");
    }

    @Test
    void testCheckOutGuest_WhenOccupied_TransitionsToFree() {
        // Arrange
        room.makeReservation();
        room.checkInGuest(guest);

        // Act
        room.checkOutGuest();

        // Assert
        assertEquals(RoomState.FREE, room.getState(), "Room state should transition to FREE");
        assertNull(room.getOccupant(), "Occupant should be null after checkout");
    }

    @Test
    void testCheckOutGuest_WhenNotOccupied_ThrowsException() {
        // Arrange
        // Room is FREE

        // Act & Assert
        assertThrows(HotelException.class, () -> room.checkOutGuest(),
                "Checking out from non-occupied room should throw HotelException");
    }

    @Test
    void testCancelReservation_WhenReserved_TransitionsToFree() {
        // Arrange
        room.makeReservation();

        // Act
        room.cancelReservation();

        // Assert
        assertEquals(RoomState.FREE, room.getState(), "Room state should transition to FREE");
    }

    @Test
    void testCancelReservation_WhenNotReserved_ThrowsException() {
        // Arrange
        // Room is FREE

        // Act & Assert
        assertThrows(HotelException.class, () -> room.cancelReservation(),
                "Cancelling reservation on non-reserved room should throw HotelException");
    }

    @Test
    void testCompleteRoomLifecycle() {
        // Arrange
        // Room starts as FREE

        // Act & Assert - Complete lifecycle
        assertEquals(RoomState.FREE, room.getState(), "Initial state should be FREE");

        room.makeReservation();
        assertEquals(RoomState.RESERVED, room.getState(), "After reservation should be RESERVED");

        room.checkInGuest(guest);
        assertEquals(RoomState.OCCUPIED, room.getState(), "After check-in should be OCCUPIED");

        room.checkOutGuest();
        assertEquals(RoomState.FREE, room.getState(), "After checkout should be FREE");
    }
}
