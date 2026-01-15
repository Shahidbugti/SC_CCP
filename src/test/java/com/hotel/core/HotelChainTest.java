package com.hotel.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.hotel.domain.*;
import com.hotel.exception.HotelException;

class HotelChainTest {

    private HotelChain chain;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        chain = new HotelChain("Chain 1");
        hotel = new Hotel("Hotel 1");
        chain.addHotel(hotel);

        RoomType type = new RoomType(RoomKind.DOUBLE, new Money(BigDecimal.TEN, Currency.getInstance("USD")));
        hotel.addRoom(new Room(101, type));
    }

    @Test
    void testAddHotel_ValidHotel_Success() {
        // Arrange
        // (Hotel added in @BeforeEach)

        // Act
        int hotelCount = chain.getHotels().size();
        String hotelName = chain.getHotels().get(0).getName();

        // Assert
        assertEquals(1, hotelCount, "Chain should contain exactly one hotel");
        assertEquals("Hotel 1", hotelName, "Hotel name should match");
    }

    @Test
    void testAddHotel_NullHotel_ThrowsException() {
        // Arrange
        Hotel nullHotel = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> chain.addHotel(nullHotel),
                "Adding null hotel should throw IllegalArgumentException");
    }

    @ParameterizedTest
    @ValueSource(strings = { "Hotel A", "Hotel B", "Hotel C" })
    void testAddHotel_MultipleHotels_Success(String hotelName) {
        // Arrange
        Hotel newHotel = new Hotel(hotelName);

        // Act
        chain.addHotel(newHotel);

        // Assert
        assertTrue(chain.getHotels().contains(newHotel),
                "Chain should contain hotel: " + hotelName);
    }

    @Test
    void testMakeReservation_DelegatesToHotel_Success() {
        // Arrange
        ReserverPayer payer = chain.createReserverPayer(new Identity("id", "1"),
                new CreditCard("1234567890123", "12/25", "123"));
        RoomType type = hotel.getRooms().get(0).getRoomType();

        // Act
        Reservation res = chain.makeReservation("Hotel 1", LocalDate.now(), LocalDate.now().plusDays(1), type, payer);

        // Assert
        assertNotNull(res, "Reservation should be created");
        assertEquals(1, hotel.getReservations().size(), "Hotel should have one reservation");
    }

    @Test
    void testMakeReservation_HotelNotFound_ThrowsException() {
        // Arrange
        ReserverPayer payer = chain.createReserverPayer(new Identity("id", "1"),
                new CreditCard("1234567890123", "12/25", "123"));
        RoomType type = hotel.getRooms().get(0).getRoomType();

        // Act & Assert
        assertThrows(HotelException.class,
                () -> chain.makeReservation("Nonexistent Hotel", LocalDate.now(),
                        LocalDate.now().plusDays(1), type, payer),
                "Making reservation at non-existent hotel should throw HotelException");
    }

    @Test
    void testCancelReservation_ValidReservation_Success() {
        // Arrange
        ReserverPayer payer = chain.createReserverPayer(new Identity("id", "1"),
                new CreditCard("1234567890123", "12/25", "123"));
        RoomType type = hotel.getRooms().get(0).getRoomType();
        Reservation res = chain.makeReservation("Hotel 1", LocalDate.now(),
                LocalDate.now().plusDays(1), type, payer);

        // Act
        chain.cancelReservation("Hotel 1", res.getReservationNumber());

        // Assert
        assertEquals(0, hotel.getReservations().size(), "Hotel should have no reservations");
    }

    @Test
    void testCheckInGuest_ValidRoom_Success() {
        // Arrange
        ReserverPayer payer = chain.createReserverPayer(new Identity("id", "1"),
                new CreditCard("1234567890123", "12/25", "123"));
        RoomType type = hotel.getRooms().get(0).getRoomType();
        chain.makeReservation("Hotel 1", LocalDate.now(), LocalDate.now().plusDays(1), type, payer);
        Guest guest = new Guest("John Doe", new Address("St", "City", "Zip"), new Identity("P", "1"));

        // Act
        chain.checkInGuest("Hotel 1", 101, guest);

        // Assert
        assertEquals(RoomState.OCCUPIED, hotel.getRooms().get(0).getState(),
                "Room should be in OCCUPIED state");
    }

    @Test
    void testCheckOutGuest_ValidRoom_Success() {
        // Arrange
        ReserverPayer payer = chain.createReserverPayer(new Identity("id", "1"),
                new CreditCard("1234567890123", "12/25", "123"));
        RoomType type = hotel.getRooms().get(0).getRoomType();
        chain.makeReservation("Hotel 1", LocalDate.now(), LocalDate.now().plusDays(1), type, payer);
        Guest guest = new Guest("John Doe", new Address("St", "City", "Zip"), new Identity("P", "1"));
        chain.checkInGuest("Hotel 1", 101, guest);

        // Act
        chain.checkOutGuest("Hotel 1", 101);

        // Assert
        assertEquals(RoomState.FREE, hotel.getRooms().get(0).getState(),
                "Room should be in FREE state after checkout");
    }

    @Test
    void testCreateReserverPayer_ValidInputs_Success() {
        // Arrange
        Identity identity = new Identity("Passport", "ABC123");
        CreditCard creditCard = new CreditCard("1234567890123456", "12/25", "123");

        // Act
        ReserverPayer payer = chain.createReserverPayer(identity, creditCard);

        // Assert
        assertNotNull(payer, "ReserverPayer should be created");
        assertEquals(identity, payer.getId(), "Identity should match");
        assertEquals(creditCard, payer.getCreditCardDetails(), "CreditCard should match");
    }
}
