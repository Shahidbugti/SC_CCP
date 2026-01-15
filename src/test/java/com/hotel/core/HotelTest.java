package com.hotel.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.hotel.domain.*;
import com.hotel.exception.HotelException;

class HotelTest {

    private Hotel hotel;
    private RoomType doubleRoomType;
    private ReserverPayer payer;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Test Hotel");
        Currency usd = Currency.getInstance("USD");
        doubleRoomType = new RoomType(RoomKind.DOUBLE, new Money(new BigDecimal("100"), usd));

        Room room1 = new Room(101, doubleRoomType);
        hotel.addRoom(room1);

        payer = new ReserverPayer(new Identity("P", "123"), new CreditCard("1234567890123", "12/25", "111"));
    }

    @Test
    void testAvailability_WhenRoomIsFree_ReturnsTrue() {
        // Arrange
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);

        // Act
        boolean available = hotel.available(start, end, doubleRoomType);

        // Assert
        assertTrue(available, "Room should be available when free");
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3", // 2 day stay
            "5, 10", // 5 day stay
            "1, 30" // Long stay
    })
    void testAvailability_VariousDateRanges_ReturnsTrue(int startOffset, int endOffset) {
        // Arrange
        LocalDate start = LocalDate.now().plusDays(startOffset);
        LocalDate end = LocalDate.now().plusDays(endOffset);

        // Act
        boolean available = hotel.available(start, end, doubleRoomType);

        // Assert
        assertTrue(available, "Room should be available for date range: " + startOffset + " to " + endOffset);
    }

    @Test
    void testCreateReservation_WhenRoomAvailable_Success() {
        // Arrange
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);

        // Act
        Reservation res = hotel.createReservation(start, end, doubleRoomType, payer);

        // Assert
        assertNotNull(res, "Reservation should be created");
        assertEquals(101, res.getRoom().getNumber(), "Room number should match");
        assertEquals(RoomState.RESERVED, res.getRoom().getState(), "Room should be in RESERVED state");
    }

    @Test
    void testCreateReservation_DatesOverlap_ThrowsException() {
        // Arrange
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);
        hotel.createReservation(start, end, doubleRoomType, payer); // First reservation

        // Act & Assert
        assertThrows(HotelException.class, () -> hotel.createReservation(start, end, doubleRoomType, payer),
                "Creating overlapping reservation should throw HotelException");
    }

    @Test
    void testCreateReservation_RoomStateNotFree_NotAvailable() {
        // Arrange
        LocalDate start = LocalDate.now().plusDays(10);
        LocalDate end = LocalDate.now().plusDays(15);
        hotel.createReservation(start, end, doubleRoomType, payer); // Make one reservation

        // Try making another one for DIFFERENT dates
        LocalDate start2 = LocalDate.now().plusDays(20);
        LocalDate end2 = LocalDate.now().plusDays(25);

        // Act
        boolean available = hotel.available(start2, end2, doubleRoomType);

        // Assert
        assertFalse(available, "Room should be unavailable if state is RESERVED (strict UML state chart)");
    }

    @Test
    void testCancelReservation_ValidReservation_Success() {
        // Arrange
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);
        Reservation res = hotel.createReservation(start, end, doubleRoomType, payer);
        int resNum = res.getReservationNumber();

        // Act
        hotel.cancelReservation(resNum);

        // Assert
        assertEquals(RoomState.FREE, res.getRoom().getState(), "Room should be FREE after cancellation");
        assertEquals(0, hotel.getReservations().size(), "Reservations list should be empty");
    }

    @Test
    void testCancelReservation_InvalidNumber_ThrowsException() {
        // Arrange
        int invalidResNum = 999;

        // Act & Assert
        assertThrows(HotelException.class, () -> hotel.cancelReservation(invalidResNum),
                "Cancelling non-existent reservation should throw HotelException");
    }

    @Test
    void testAddRoom_ValidRoom_Success() {
        // Arrange
        RoomType singleType = new RoomType(RoomKind.SINGLE,
                new Money(new BigDecimal("75"), Currency.getInstance("USD")));
        Room newRoom = new Room(102, singleType);

        // Act
        hotel.addRoom(newRoom);

        // Assert
        assertEquals(2, hotel.getRooms().size(), "Hotel should have 2 rooms");
        assertTrue(hotel.getRooms().contains(newRoom), "Hotel should contain the new room");
    }

    @Test
    void testAddRoom_NullRoom_ThrowsException() {
        // Arrange
        Room nullRoom = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.addRoom(nullRoom),
                "Adding null room should throw IllegalArgumentException");
    }

    @Test
    void testGetName() {
        // Arrange
        // (Hotel created in @BeforeEach)

        // Act
        String name = hotel.getName();

        // Assert
        assertEquals("Test Hotel", name, "Hotel name should match");
    }

    @Test
    void testGetReservations_ReturnsUnmodifiableList() {
        // Arrange
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);
        hotel.createReservation(start, end, doubleRoomType, payer);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> hotel.getReservations().clear(),
                "Reservations list should be unmodifiable");
    }
}
