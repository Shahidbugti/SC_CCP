package com.hotel.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.hotel.domain.*;

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
    void testAddHotel() {
        assertEquals(1, chain.getHotels().size());
        assertEquals("Hotel 1", chain.getHotels().get(0).getName());
    }

    @Test
    void testMakeReservation_DelegatesToHotel() {
        ReserverPayer payer = chain.createReserverPayer(new Identity("id", "1"),
                new CreditCard("1234567890123", "12/25", "123"));
        RoomType type = hotel.getRooms().get(0).getRoomType();

        Reservation res = chain.makeReservation("Hotel 1", LocalDate.now(), LocalDate.now().plusDays(1), type, payer);

        assertNotNull(res);
        assertEquals(1, hotel.getReservations().size());
    }
}
