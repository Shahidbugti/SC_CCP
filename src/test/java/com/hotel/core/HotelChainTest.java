package com.hotel.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import com.hotel.domain.*;
import com.hotel.exception.HotelException;

class HotelChainTest {

        private HotelChain chain;
        private Hotel hotel;

        @BeforeEach
        void setUp() {
                chain = new HotelChain("Prestige Group");
                hotel = new Hotel("The Grand Budapest");
                chain.addHotel(hotel);

                // Using humanized Money constructor
                RoomType type = new RoomType(RoomKind.DOUBLE, new Money(100.0, "USD"));
                hotel.addRoom(new Room(101, type));
        }

        @Test
        void testAddHotel_ValidHotel_Success() {
                // Assert
                assertEquals(1, chain.getHotels().size(), "Chain should contain exactly one hotel");
                assertEquals("The Grand Budapest", chain.getHotels().get(0).getName(), "Hotel name should match");
        }

        @Test
        void testAddHotel_NullHotel_ThrowsException() {
                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> chain.addHotel(null),
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
                ReserverPayer payer = chain.createReserverPayer(new Identity("ID", "1"),
                                new CreditCard("1234567890123", "12/25", "123"));
                RoomType type = hotel.getRooms().get(0).getRoomType();

                // Act
                Reservation res = chain.makeReservation("The Grand Budapest", LocalDate.now(),
                                LocalDate.now().plusDays(1), type, payer);

                // Assert
                assertNotNull(res, "Reservation should be successfully created via the chain");
                assertEquals(1, hotel.getReservations().size(), "Hotel should have one reservation record");
        }

        @Test
        void testMakeReservation_HotelNotFound_ThrowsException() {
                // Arrange
                ReserverPayer payer = chain.createReserverPayer(new Identity("ID", "1"),
                                new CreditCard("1234567890123", "12/25", "123"));
                RoomType type = hotel.getRooms().get(0).getRoomType();

                // Act & Assert
                assertThrows(HotelException.class,
                                () -> chain.makeReservation("Ghost Hotel", LocalDate.now(),
                                                LocalDate.now().plusDays(1), type, payer),
                                "Making reservation at non-existent hotel should throw HotelException");
        }

        @Test
        void testCancelReservation_ValidReservation_Success() {
                // Arrange
                ReserverPayer payer = chain.createReserverPayer(new Identity("ID", "1"),
                                new CreditCard("1234567890123", "12/25", "123"));
                RoomType type = hotel.getRooms().get(0).getRoomType();
                Reservation res = chain.makeReservation("The Grand Budapest", LocalDate.now(),
                                LocalDate.now().plusDays(1), type, payer);

                // Act
                chain.cancelReservation("The Grand Budapest", res.getReservationNumber());

                // Assert
                assertEquals(0, hotel.getReservations().size(), "Hotel should have no reservations after cancellation");
        }

        @Test
        void testCheckInGuest_ValidRoom_Success() {
                // Arrange
                ReserverPayer payer = chain.createReserverPayer(new Identity("ID", "1"),
                                new CreditCard("1234567890123", "12/25", "123"));
                RoomType type = hotel.getRooms().get(0).getRoomType();
                chain.makeReservation("The Grand Budapest", LocalDate.now(), LocalDate.now().plusDays(1), type, payer);
                Guest guest = new Guest("John Doe", new Address("St", "City", "Zip"), new Identity("P", "1"));

                // Act
                chain.checkInGuest("The Grand Budapest", 101, guest);

                // Assert
                assertEquals(RoomState.OCCUPIED, hotel.getRooms().get(0).getState(),
                                "Room should be in OCCUPIED state after check-in");
        }

        @Test
        void testCheckOutGuest_ValidRoom_Success() {
                // Arrange
                ReserverPayer payer = chain.createReserverPayer(new Identity("ID", "1"),
                                new CreditCard("1234567890123", "12/25", "123"));
                RoomType type = hotel.getRooms().get(0).getRoomType();
                chain.makeReservation("The Grand Budapest", LocalDate.now(), LocalDate.now().plusDays(1), type, payer);
                Guest guest = new Guest("John Doe", new Address("St", "City", "Zip"), new Identity("P", "1"));
                chain.checkInGuest("The Grand Budapest", 101, guest);

                // Act
                chain.checkOutGuest("The Grand Budapest", 101);

                // Assert
                assertEquals(RoomState.FREE, hotel.getRooms().get(0).getState(),
                                "Room should be back to FREE state after checkout");
        }

        @Test
        void testCreateReserverPayer_ValidInputs_Success() {
                // Arrange
                Identity identity = new Identity("Passport", "ABC123");
                CreditCard creditCard = new CreditCard("123456789013", "12/25", "123");

                // Act
                ReserverPayer payer = chain.createReserverPayer(identity, creditCard);

                // Assert
                assertNotNull(payer, "ReserverPayer should be created successfully");
                assertEquals(identity, payer.getId(), "Customer identity should match");
                assertEquals(creditCard, payer.getCreditCardDetails(), "Payment details should match");
        }
}
