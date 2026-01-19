package com.hotel.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hotel.domain.CreditCard;
import com.hotel.domain.Guest;
import com.hotel.domain.Identity;
import com.hotel.domain.Reservation;
import com.hotel.domain.ReserverPayer;
import com.hotel.domain.Room;
import com.hotel.domain.RoomType;
import com.hotel.exception.HotelException;

/**
 * Manages a collection of hotels and customer payment profiles.
 */
public class HotelChain {
    private final String name;
    private final List<Hotel> hotels;
    private final List<ReserverPayer> customers;

    public HotelChain(String name) {
        validateChainName(name);
        this.name = name;
        this.hotels = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    private void validateChainName(String chainName) {
        if (chainName == null || chainName.trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel chain name is required");
        }
    }

    /**
     * Adds a new hotel to the chain's portfolio.
     */
    public void addHotel(Hotel hotel) {
        if (hotel == null) {
            throw new IllegalArgumentException("Cannot add a null hotel to the chain");
        }
        hotels.add(hotel);
    }

    public List<Hotel> getHotels() {
        return Collections.unmodifiableList(hotels);
    }

    /**
     * Registers a new customer (ReserverPayer) in the system.
     */
    public ReserverPayer createReserverPayer(Identity id, CreditCard creditCard) {
        ReserverPayer newCustomer = new ReserverPayer(id, creditCard);
        customers.add(newCustomer);
        return newCustomer;
    }

    /**
     * Attempts to book a room in a specific hotel.
     */
    public Reservation makeReservation(String hotelName, LocalDate start, LocalDate end,
            RoomType roomType, ReserverPayer customer) {
        Hotel targetHotel = findHotelByName(hotelName);

        // Double check availability before creating reservation
        if (!targetHotel.available(start, end, roomType)) {
            throw new HotelException("Sorry, no " + roomType.getKind() + " rooms available in " + hotelName);
        }

        return targetHotel.createReservation(start, end, roomType, customer);
    }

    /**
     * Cancels an existing reservation in the specified hotel.
     */
    public void cancelReservation(String hotelName, int reservationId) {
        findHotelByName(hotelName).cancelReservation(reservationId);
    }

    /**
     * Performs guest check-in at a specific hotel and room.
     */
    public void checkInGuest(String hotelName, int roomNumber, Guest guest) {
        Hotel hotel = findHotelByName(hotelName);
        Room room = findRoomInHotel(hotel, roomNumber);

        room.checkInGuest(guest);
    }

    /**
     * Performs guest check-out at a specific hotel and room.
     */
    public void checkOutGuest(String hotelName, int roomNumber) {
        Hotel hotel = findHotelByName(hotelName);
        Room room = findRoomInHotel(hotel, roomNumber);

        room.checkOutGuest();
    }

    /**
     * Helper to find a hotel by its name (case-insensitive).
     */
    private Hotel findHotelByName(String name) {
        return hotels.stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new HotelException("Hotel '" + name + "' does not belong to this chain"));
    }

    /**
     * Helper to find a specific room within a hotel.
     */
    private Room findRoomInHotel(Hotel hotel, int number) {
        return hotel.getRooms().stream()
                .filter(r -> r.getNumber() == number)
                .findFirst()
                .orElseThrow(() -> new HotelException("Room " + number + " not found in " + hotel.getName()));
    }

    public String getName() {
        return name;
    }
}
