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
 * Demonstrates the Hotel Reservation System.
 * This entry point shows how the system handles bookings, check-ins,
 * and defensive validation.
 */
public class Main {
    public static void main(String[] args) {
        printHeader();

        try {
            // 1. INITIALIZE SYSTEM
            System.out.println(">>> STEP 1: Setting up the Hotel Chain and Inventory");
            HotelChain luxuryChain = new HotelChain("Global Hotel Group");
            Hotel grandBudapest = new Hotel("The Grand Budapest");
            luxuryChain.addHotel(grandBudapest);

            // Setup Room Types with localized pricing
            RoomType doubleType = new RoomType(RoomKind.DOUBLE, new Money(150.00, "USD"));
            RoomType familyType = new RoomType(RoomKind.FAMILY, new Money(250.00, "USD"));

            // Add Rooms to the Hotel
            grandBudapest.addRoom(new Room(101, doubleType));
            grandBudapest.addRoom(new Room(102, doubleType));
            grandBudapest.addRoom(new Room(201, familyType));

            System.out.println("System initialized with " + grandBudapest.getRooms().size() + " rooms.");
            System.out.println();

            // 2. REGISTER CUSTOMERS
            System.out.println(">>> STEP 2: Registering a New Customer");
            Address homeAddress = new Address("123 Baker St", "London", "NW1 6XE");
            Identity passport = new Identity("Passport", "UK-123456789");
            CreditCard card = new CreditCard("4444-5555-6666-7777", "12/28", "123");

            Guest mainGuest = new Guest("John Doe", homeAddress, passport);
            ReserverPayer customer = luxuryChain.createReserverPayer(passport, card);

            System.out.println("Customer registered: " + mainGuest.getName());
            System.out.println();

            // 3. BOOKING PROCESS
            System.out.println(">>> STEP 3: Making a Reservation");
            LocalDate checkIn = LocalDate.now().plusWeeks(1);
            LocalDate checkOut = LocalDate.now().plusWeeks(2);

            System.out.println("Booking DOUBLE room from " + checkIn + " to " + checkOut);
            Reservation reservation1 = luxuryChain.makeReservation(
                    "The Grand Budapest", checkIn, checkOut, doubleType, customer);

            System.out.println("SUCCESS: Reservation #" + reservation1.getReservationNumber() + " confirmed.");
            System.out.println("Assigned Room: " + reservation1.getRoom().getNumber());
            System.out.println("Current Room State: " + reservation1.getRoom().getState());
            System.out.println();

            // 4. DEFENSIVE PROGRAMMING DEMO
            System.out.println(">>> STEP 4: Defensive Programming Demo (Overbooking)");

            // This second booking should succeed (we have 2 double rooms)
            System.out.println("Booking second available DOUBLE room...");
            luxuryChain.makeReservation("The Grand Budapest", checkIn, checkOut, doubleType, customer);

            // This third booking should fail (only 2 double rooms exist)
            System.out.println("Attempting to book a THIRD DOUBLE room (expecting failure)...");
            try {
                luxuryChain.makeReservation("The Grand Budapest", checkIn, checkOut, doubleType, customer);
            } catch (HotelException e) {
                System.out.println("CAUGHT EXPECTED ERROR: " + e.getMessage());
            }
            System.out.println();

            // 5. GUEST LIFE CYCLE (CHECK-IN / CHECK-OUT)
            System.out.println(">>> STEP 5: Guest Life Cycle");

            System.out.println("Checking in " + mainGuest.getName() + " to Room " + reservation1.getRoom().getNumber());
            luxuryChain.checkInGuest("The Grand Budapest", reservation1.getRoom().getNumber(), mainGuest);
            System.out.println("Room State now: " + reservation1.getRoom().getState());

            System.out.println("Checking out guest...");
            luxuryChain.checkOutGuest("The Grand Budapest", reservation1.getRoom().getNumber());
            System.out.println("Room State now: " + reservation1.getRoom().getState());
            System.out.println();

            // 6. CANCELLATION
            System.out.println(">>> STEP 6: Cancellation Demo");
            System.out.println("Wait, Room " + reservation1.getRoom().getNumber() + " is already FREE after checkout.");
            System.out.println("Let's book another room and then cancel it to show the flow.");

            Reservation reservation3 = luxuryChain.makeReservation(
                    "The Grand Budapest", checkIn, checkOut, familyType, customer);
            System.out.println("Booked Family Room #" + reservation3.getRoom().getNumber());
            System.out.println("Cancelling Reservation #" + reservation3.getReservationNumber() + "...");
            luxuryChain.cancelReservation("The Grand Budapest", reservation3.getReservationNumber());
            System.out.println("Family Room is now: " + reservation3.getRoom().getState());

            printFooter();

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR DURING DEMO:");
            e.printStackTrace();
        }
    }

    private static void printHeader() {
        System.out.println("*********************************************************");
        System.out.println("*             HOTEL RESERVATION SYSTEM DEMO             *");
        System.out.println("*********************************************************");
        System.out.println();
    }

    private static void printFooter() {
        System.out.println("*********************************************************");
        System.out.println("*            END OF SYSTEM DEMONSTRATION                *");
        System.out.println("*********************************************************");
    }
}
