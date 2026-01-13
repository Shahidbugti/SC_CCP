package com.hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import com.hotel.core.Hotel;
import com.hotel.core.HotelChain;
import com.hotel.domain.Address;
import com.hotel.domain.CreditCard;
import com.hotel.domain.Guest;
import com.hotel.domain.Identity;
import com.hotel.domain.Money;
import com.hotel.domain.Reservation;
import com.hotel.domain.ReserverPayer;
import com.hotel.domain.Room;
import com.hotel.domain.RoomKind;
import com.hotel.domain.RoomType;
import com.hotel.exception.HotelException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hotel Room Reservation System Demonstration ===\n");

        try {
            // 1. Object Creation
            System.out.println("1. Initializing System Application...");
            HotelChain chain = new HotelChain("Global Hotels");
            Hotel hotel = new Hotel("Grand Budapest");
            chain.addHotel(hotel);

            // Create Attributes
            Currency usd = Currency.getInstance("USD");
            Money costDouble = new Money(new BigDecimal("150.00"), usd);
            Money costFamily = new Money(new BigDecimal("250.00"), usd);

            RoomType doubleRoomType = new RoomType(RoomKind.DOUBLE, costDouble);
            RoomType familyRoomType = new RoomType(RoomKind.FAMILY, costFamily);

            // Create Rooms
            Room room101 = new Room(101, doubleRoomType);
            Room room102 = new Room(102, familyRoomType);
            hotel.addRoom(room101);
            hotel.addRoom(room102);

            System.out.println("Initialized Hotel: " + hotel.getName() + " with " + hotel.getRooms().size() + " rooms.");

            // Create Actors
            Address address = new Address("123 Baker St", "London", "NW1 6XE");
            Identity id = new Identity("Passport", "A12345678");
            Guest guest = new Guest("John Doe", address, id);

            CreditCard cc = new CreditCard("1234567812345678", "12/26", "123");
            ReserverPayer payer = chain.createReserverPayer(id, cc);

            System.out.println("Created Guest: " + guest.getName());
            System.out.println("Created Payer: " + payer.getId());
            System.out.println("----------------------------------------------\n");

            // 2. Successful Reservation
            LocalDate start = LocalDate.now().plusDays(1);
            LocalDate end = LocalDate.now().plusDays(5);

            System.out.println("2. Attempting Reservation for " + RoomKind.DOUBLE + " from " + start + " to " + end);

            Reservation res = chain.makeReservation("Grand Budapest", start, end, doubleRoomType, payer);
            System.out.println("SUCCESS: Reservation created. Number: " + res.getReservationNumber() + ", Room: " + res.getRoom().getNumber());
            System.out.println("Room " + res.getRoom().getNumber() + " state: " + res.getRoom().getState());
            System.out.println("----------------------------------------------\n");

            // 3. Check-In
            System.out.println("3. Checking in Guest...");
            // Strictly speaking, room must be reserved.
            chain.checkInGuest("Grand Budapest", res.getRoom().getNumber(), guest);
            System.out.println("SUCCESS: Guest checked in.");
            System.out.println("Room " + res.getRoom().getNumber() + " state: " + res.getRoom().getState());
            System.out.println("----------------------------------------------\n");

            // 4. Invalid Reservation (Defensive Programming)
            System.out.println("4. Attempting Invalid Reservation (Room already occupied/reserved)...");
            // Try to book the same room for overlapping dates
            try {
                chain.makeReservation("Grand Budapest", start.plusDays(1), end.plusDays(1), doubleRoomType, payer);
            } catch (HotelException e) {
                System.out.println("EXPECTED ERROR (Defensive Programming): " + e.getMessage());
            }

            // 5. Invalid Input (Defensive Programming)
            System.out.println("\n5. Attempting Invalid Input (Null Name)...");
            try {
                new Guest(null, address, id);
            } catch (IllegalArgumentException e) {
                System.out.println("EXPECTED ERROR (Validation): " + e.getMessage());
            }

            System.out.println("----------------------------------------------\n");
            System.out.println("Demonstration Complete.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
