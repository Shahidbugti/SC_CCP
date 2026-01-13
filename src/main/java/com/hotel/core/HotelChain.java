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

public class HotelChain {
    private final String name;
    private final List<Hotel> hotels;
    private final List<ReserverPayer> payers;

    public HotelChain(String name) {
        if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Chain name cannot be empty");
		}
        this.name = name;
        this.hotels = new ArrayList<>();
        this.payers = new ArrayList<>();
    }

    public void addHotel(Hotel hotel) {
        if (hotel == null) {
			throw new IllegalArgumentException("Hotel cannot be null");
		}
        hotels.add(hotel);
    }

    public List<Hotel> getHotels() {
        return Collections.unmodifiableList(hotels);
    }

    public ReserverPayer createReserverPayer(Identity id, CreditCard cc) {
        ReserverPayer payer = new ReserverPayer(id, cc);
        payers.add(payer);
        return payer;
    }

    public Reservation makeReservation(String hotelName, LocalDate start, LocalDate end, RoomType roomType, ReserverPayer payer) {
        Hotel hotel = findHotel(hotelName);
        if (hotel.available(start, end, roomType)) {
            return hotel.createReservation(start, end, roomType, payer);
        } else {
            throw new HotelException("Room not available in " + hotelName);
        }
    }

    public void cancelReservation(String hotelName, int reservationNumber) {
        Hotel hotel = findHotel(hotelName);
        hotel.cancelReservation(reservationNumber);
    }

    public void checkInGuest(String hotelName, int roomNumber, Guest guest) {
        Hotel hotel = findHotel(hotelName);
        Room room = hotel.getRooms().stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst()
                .orElseThrow(() -> new HotelException("Room " + roomNumber + " not found in " + hotelName));

        room.checkInGuest(guest);
    }

    public void checkOutGuest(String hotelName, int roomNumber) {
        Hotel hotel = findHotel(hotelName);
        Room room = hotel.getRooms().stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst()
                .orElseThrow(() -> new HotelException("Room " + roomNumber + " not found in " + hotelName));

        room.checkOutGuest();
    }

    private Hotel findHotel(String name) {
        return hotels.stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new HotelException("Hotel not found: " + name));
    }
}
