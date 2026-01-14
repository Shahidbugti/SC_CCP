package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

class ReservationTest {

    @Test
    void testReservationCreation_Success() {
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"), new CreditCard("123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        Reservation res = new Reservation(1, start, end, payer, room);

        assertEquals(1, res.getReservationNumber());
        assertEquals(start, res.getStartDate());
        assertEquals(end, res.getEndDate());
        assertEquals(payer, res.getPayer());
        assertEquals(room, res.getRoom());
    }

    @Test
    void testReservationCreation_InvalidDates() {
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"), new CreditCard("123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().minusDays(1); // End before start

        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, start, end, payer, room);
        });
    }

    @Test
    void testReservationCreation_NullInputs() {
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"), new CreditCard("123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        // Null payer
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, start, end, null, room);
        });

        // Null room
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1, start, end, payer, null);
        });
    }
}
