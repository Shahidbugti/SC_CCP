package com.hotel.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testAvailability_Success() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);

        assertTrue(hotel.available(start, end, doubleRoomType), "Room should be available");
    }

    @Test
    void testCreateReservation_Success() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);

        Reservation res = hotel.createReservation(start, end, doubleRoomType, payer);

        assertNotNull(res);
        assertEquals(101, res.getRoom().getNumber());
        assertEquals(RoomState.RESERVED, res.getRoom().getState());
    }

    @Test
    void testCreateReservation_NoAvailability_DatesOverlap() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);

        // First reservation
        hotel.createReservation(start, end, doubleRoomType, payer);

        // Second attempt overlapping
        assertThrows(HotelException.class, () -> {
            hotel.createReservation(start, end, doubleRoomType, payer);
        });
    }

    @Test
    void testCreateReservation_RoomStateNotFree() {
        // Manually set room state to RESERVED (simulate existing blocking)
        LocalDate start = LocalDate.now().plusDays(10);
        LocalDate end = LocalDate.now().plusDays(15);

        // Make one reservation
        hotel.createReservation(start, end, doubleRoomType, payer);

        // Try making another one for DIFFERENT dates
        // Due to strict UML state chart (Room state is single value RESERVED), this
        // should fail
        // because Room is not FREE.
        LocalDate start2 = LocalDate.now().plusDays(20);
        LocalDate end2 = LocalDate.now().plusDays(25);

        assertFalse(hotel.available(start2, end2, doubleRoomType),
                "Room should strict be unavailable if state is RESERVED");
    }

    @Test
    void testCancelReservation() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);
        Reservation res = hotel.createReservation(start, end, doubleRoomType, payer);

        int resNum = res.getReservationNumber();
        hotel.cancelReservation(resNum);

        assertEquals(RoomState.FREE, res.getRoom().getState());
        assertEquals(0, hotel.getReservations().size());
    }
}
