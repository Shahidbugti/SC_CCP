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

/**
 * Main application entry point for the Hotel Reservation System.
 * Demonstrates the core use cases and defensive programming measures.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("       HOTEL RESERVATION SYSTEM - DEMONSTRATION");
        System.out.println("=========================================================\n");

        try {
            // =================================================================
            // 1. SETUP & INITIALIZATION
            // =================================================================
            System.out.println("--- [1] System Initialization ---");

            // 1.1 Create Hotel Chain
            HotelChain chain = new HotelChain("Global Luxury Hotels");
            System.out.println("Created HotelChain: " + "Global Luxury Hotels");

            // 1.2 Create Hotel
            Hotel hotel = new Hotel("The Grand Budapest");
            chain.addHotel(hotel);
            System.out.println("Created Hotel: " + hotel.getName());

            // 1.3 Define Room Types and Costs
            Currency usd = Currency.getInstance("USD");
            RoomType doubleType = new RoomType(RoomKind.DOUBLE, new Money(new BigDecimal("150.00"), usd));
            RoomType familyType = new RoomType(RoomKind.FAMILY, new Money(new BigDecimal("250.00"), usd));

            // 1.4 Create Rooms and Add to Hotel
            // Adding a few rooms to allow for testing availability failures
            Room room101 = new Room(101, doubleType);
            Room room102 = new Room(102, doubleType); // Another double room
            Room room201 = new Room(201, familyType);

            hotel.addRoom(room101);
            hotel.addRoom(room102);
            hotel.addRoom(room201);

            System.out.println("Added Room #101 (DOUBLE)");
            System.out.println("Added Room #102 (DOUBLE)");
            System.out.println("Added Room #201 (FAMILY)");
            System.out.println("Initialization Complete.\n");

            // =================================================================
            // 2. ACTOR CREATION
            // =================================================================
            System.out.println("--- [2] Actor Creation ---");

            Address address = new Address("123 Baker St", "London", "NW1 6XE");
            Identity identity = new Identity("Passport", "UK-123456789");
            CreditCard creditCard = new CreditCard("4444-5555-6666-7777", "12/28", "123");

            // Create Guest
            Guest guest = new Guest("John Doe", address, identity);
            System.out.println("Created Guest: " + guest.getName());

            // Create ReserverPayer (The person paying implies they are registered in the
            // chain)
            ReserverPayer payer = chain.createReserverPayer(identity, creditCard);
            System.out.println("Created ReserverPayer with ID: " + payer.getId().getIdNumber());
            System.out.println();

            // =================================================================
            // 3. USE CASE: MAKE RESERVATION
            // =================================================================
            System.out.println("--- [3] Use Case: Make Reservation ---");

            LocalDate startDate = LocalDate.now().plusDays(5);
            LocalDate endDate = LocalDate.now().plusDays(10);

            System.out
                    .println("Requesting reservation for " + RoomKind.DOUBLE + " from " + startDate + " to " + endDate);

            Reservation res1 = chain.makeReservation("The Grand Budapest", startDate, endDate, doubleType, payer);

            System.out.println(">>> CHECK: Reservation Created Successfully.");
            System.out.println("    Reservation #: " + res1.getReservationNumber());
            System.out.println("    Assigned Room: " + res1.getRoom().getNumber());
            System.out.println("    Room State:    " + res1.getRoom().getState() + " (Expected: RESERVED)");
            System.out.println();

            // =================================================================
            // 4. USE CASE: CHECK AVAILABILITY & DEFENSIVE PROGRAMMING
            // =================================================================
            System.out.println("--- [4] Use Case: Availability & Defensive Checks ---");

            // 4.1 Try to book the exact same room type/dates (We have 2 double rooms, so
            // this SHOULD succeed with Room 102)
            System.out.println("Test A: Booking second available DOUBLE room...");
            Reservation res2 = chain.makeReservation("The Grand Budapest", startDate, endDate, doubleType, payer);
            System.out.println(">>> CHECK: Second Reservation Created. Assigned Room: " + res2.getRoom().getNumber());

            // 4.2 Try to book a THIRD DOUBLE room (Only 2 exist, both RESERVED)
            System.out.println("Test B: Attempting to book 3rd DOUBLE room (Expect Failure)...");
            try {
                chain.makeReservation("The Grand Budapest", startDate, endDate, doubleType, payer);
            } catch (HotelException e) {
                System.out.println(">>> EXPECTED ERROR CAUGHT: " + e.getMessage());
            }

            // 4.3 Check Availability Method directly
            boolean available = hotel.available(startDate, endDate, familyType);
            System.out.println("Test C: Checking availability for FAMILY room (Should be True): " + available);
            System.out.println();

            // =================================================================
            // 5. USE CASE: CHECK-IN GUEST
            // =================================================================
            System.out.println("--- [5] Use Case: Check-In Guest ---");

            System.out.println("Checking in guest to Room " + res1.getRoom().getNumber());
            chain.checkInGuest("The Grand Budapest", res1.getRoom().getNumber(), guest);

            System.out.println(">>> CHECK: Guest Checked In.");
            System.out.println("    Room State: " + res1.getRoom().getState() + " (Expected: OCCUPIED)");
            System.out.println("    Occupant:   " + res1.getRoom().getOccupant().getName());

            // 5.1 Defensive Check: Try checking in to a non-reserved room (Room 201 - Free)
            System.out.println("Test D: Attempt check-in to unreserved Room 201 (Expect Failure)...");
            try {
                chain.checkInGuest("The Grand Budapest", 201, guest);
            } catch (HotelException e) {
                System.out.println(">>> EXPECTED ERROR CAUGHT: " + e.getMessage());
            }
            System.out.println();

            // =================================================================
            // 6. USE CASE: CHECK-OUT GUEST
            // =================================================================
            System.out.println("--- [6] Use Case: Check-Out Guest ---");

            System.out.println("Checking out from Room " + res1.getRoom().getNumber());
            chain.checkOutGuest("The Grand Budapest", res1.getRoom().getNumber());

            System.out.println(">>> CHECK: Guest Checked Out.");
            System.out.println("    Room State: " + res1.getRoom().getState() + " (Expected: FREE)");
            System.out.println();

            // =================================================================
            // 7. USE CASE: CANCEL RESERVATION
            // =================================================================
            System.out.println("--- [7] Use Case: Cancel Reservation ---");

            // Use res2 which is still RESERVED
            System.out.println("Cancelling Reservation #" + res2.getReservationNumber() + " (Room "
                    + res2.getRoom().getNumber() + ")");
            chain.cancelReservation("The Grand Budapest", res2.getReservationNumber());

            System.out.println(">>> CHECK: Reservation Cancelled.");
            System.out.println("    Room State: " + res2.getRoom().getState() + " (Expected: FREE)");
            System.out.println();

            // =================================================================
            // 8. FINAL STATE SUMMARY
            // =================================================================
            System.out.println("--- [8] Final System State ---");
            hotel.getRooms().forEach(r -> System.out
                    .println("Room " + r.getNumber() + " (" + r.getRoomType().getKind() + "): " + r.getState()));
            System.out.println("Total Reservations in System: " + hotel.getReservations().size()
                    + " (Should be 0 after cleanup, or 1 if Res1 is kept - wait, R1 was checked out, R2 cancelled)");
            System.out.println("\nDemo Completed Successfully.");

        } catch (Exception e) {
            System.err.println("!!! UNEXPECTED EXCEPTION !!!");
            e.printStackTrace();
        }
    }
}
