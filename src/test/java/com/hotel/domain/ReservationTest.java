package com.hotel.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

class ReservationTest {

    @Test
    void testReservationCreation_ValidInputs_Success() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        // Act
        Reservation res = new Reservation(1, start, end, payer, room);

        // Assert
        assertEquals(1, res.getReservationNumber(), "Reservation number should match");
        assertEquals(start, res.getStartDate(), "Start date should match");
        assertEquals(end, res.getEndDate(), "End date should match");
        assertEquals(payer, res.getPayer(), "Payer should match");
        assertEquals(room, res.getRoom(), "Room should match");
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2", // 1 day stay
            "1, 7", // 1 week stay
            "1, 30", // 1 month stay
            "5, 10" // 5 days stay
    })
    void testReservationCreation_VariousDateRanges_Success(int startOffset, int endOffset) {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now().plusDays(startOffset);
        LocalDate end = LocalDate.now().plusDays(endOffset);

        // Act
        Reservation res = new Reservation(1, start, end, payer, room);

        // Assert
        assertNotNull(res, "Reservation should be created for date range: " + startOffset + " to " + endOffset);
        assertTrue(res.getEndDate().isAfter(res.getStartDate()), "End date should be after start date");
    }

    @Test
    void testReservationCreation_EndBeforeStart_ThrowsException() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().minusDays(1); // End before start

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Reservation(1, start, end, payer, room),
                "Creating reservation with end date before start date should throw IllegalArgumentException");
    }

    @Test
    void testReservationCreation_NullStartDate_ThrowsException() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate end = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Reservation(1, null, end, payer, room),
                "Creating reservation with null start date should throw IllegalArgumentException");
    }

    @Test
    void testReservationCreation_NullEndDate_ThrowsException() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Reservation(1, start, null, payer, room),
                "Creating reservation with null end date should throw IllegalArgumentException");
    }

    @Test
    void testReservationCreation_NullPayer_ThrowsException() {
        // Arrange
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Reservation(1, start, end, null, room),
                "Creating reservation with null payer should throw IllegalArgumentException");
    }

    @Test
    void testReservationCreation_NullRoom_ThrowsException() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Reservation(1, start, end, payer, null),
                "Creating reservation with null room should throw IllegalArgumentException");
    }

    @Test
    void testReservationEquality_SameReservationNumber() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        Reservation res1 = new Reservation(1, start, end, payer, room);
        Reservation res2 = new Reservation(1, start.plusDays(5), end.plusDays(5), payer, room);

        // Act & Assert
        assertEquals(res1, res2, "Reservations with same number should be equal");
        assertEquals(res1.hashCode(), res2.hashCode(), "Equal reservations should have same hashCode");
    }

    @Test
    void testReservationEquality_DifferentReservationNumber() {
        // Arrange
        ReserverPayer payer = new ReserverPayer(new Identity("P", "1"),
                new CreditCard("1234567890123", "12/12", "123"));
        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        Room room = new Room(101, type);
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);

        Reservation res1 = new Reservation(1, start, end, payer, room);
        Reservation res2 = new Reservation(2, start, end, payer, room);

        // Act & Assert
        assertNotEquals(res1, res2, "Reservations with different numbers should not be equal");
    }
}
