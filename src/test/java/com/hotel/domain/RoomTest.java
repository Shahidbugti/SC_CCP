package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testInitialState() {
        assertEquals(RoomState.FREE, room.getState());
    }

    @Test
    void testMakeReservation_Success() {
        room.makeReservation();
        assertEquals(RoomState.RESERVED, room.getState());
    }

    @Test
    void testMakeReservation_FailWhenNotFree() {
        room.makeReservation(); // State -> RESERVED

        assertThrows(HotelException.class, () -> {
            room.makeReservation();
        });
    }

    @Test
    void testCheckIn_Success() {
        room.makeReservation();
        room.checkInGuest(guest);
        assertEquals(RoomState.OCCUPIED, room.getState());
        assertEquals(guest, room.getOccupant());
    }

    @Test
    void testCheckIn_FailWhenNotReserved() {
        // Room is FREE
        assertThrows(HotelException.class, () -> {
            room.checkInGuest(guest);
        });
    }

    @Test
    void testCheckOut_Success() {
        room.makeReservation();
        room.checkInGuest(guest);
        room.checkOutGuest();

        assertEquals(RoomState.FREE, room.getState());
        assertNull(room.getOccupant());
    }
}
