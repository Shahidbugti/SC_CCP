package com.hotel.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.hotel.domain.Reservation;
import com.hotel.domain.ReserverPayer;
import com.hotel.domain.Room;
import com.hotel.domain.RoomType;
import com.hotel.exception.HotelException;
import com.hotel.domain.RoomState;

/**
 * Represents a hotel with rooms and reservation management.
 */
public class Hotel {
    private final String name;
    private final List<Room> rooms;
    private final List<Reservation> reservations;

    public Hotel(String name) {
        validateHotelName(name);

        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    /**
     * Validates that hotel name is provided.
     */
    private void validateHotelName(String hotelName) {
        if (hotelName == null || hotelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel name is required");
        }
    }

    /**
     * Adds a room to this hotel.
     */
    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        rooms.add(room);
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if a room of the given type is available for the specified dates.
     * A room is available if:
     * 1. It's currently in FREE state
     * 2. No existing reservations overlap with the requested dates
     */
    public boolean available(LocalDate startDate, LocalDate endDate, RoomType roomType) {
        return findAvailableRoom(startDate, endDate, roomType).isPresent();
    }

    /**
     * Finds an available room of the specified type for the given dates.
     */
    private Optional<Room> findAvailableRoom(LocalDate startDate, LocalDate endDate, RoomType roomType) {
        return rooms.stream()
                .filter(room -> room.getRoomType().equals(roomType))
                .filter(room -> canBookRoom(room, startDate, endDate))
                .findFirst();
    }

    /**
     * Checks if a specific room can be booked for the given dates.
     * Returns true if room is free and has no date conflicts.
     */
    private boolean canBookRoom(Room room, LocalDate startDate, LocalDate endDate) {
        // First check: Room must be in FREE state
        if (!room.isFree()) {
            return false;
        }

        // Second check: No date overlap with existing reservations
        return !hasDateConflict(room, startDate, endDate);
    }

    /**
     * Checks if the room has any reservation that overlaps with the given dates.
     */
    private boolean hasDateConflict(Room room, LocalDate startDate, LocalDate endDate) {
        for (Reservation existingReservation : reservations) {
            if (existingReservation.getRoom().equals(room)) {
                if (datesOverlap(startDate, endDate, existingReservation)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if two date ranges overlap.
     * Overlap occurs when: (StartA < EndB) AND (EndA > StartB)
     */
    private boolean datesOverlap(LocalDate startDate, LocalDate endDate, Reservation reservation) {
        return startDate.isBefore(reservation.getEndDate()) &&
                endDate.isAfter(reservation.getStartDate());
    }

    /**
     * Creates a new reservation for the specified room type and dates.
     * This will book an available room and create a reservation record.
     */
    public Reservation createReservation(LocalDate startDate, LocalDate endDate,
            RoomType roomType, ReserverPayer payer) {
        // Find an available room
        Optional<Room> availableRoom = findAvailableRoom(startDate, endDate, roomType);

        if (availableRoom.isEmpty()) {
            String errorMsg = String.format(
                    "No rooms of type %s available for the requested dates",
                    roomType.getKind());
            throw new HotelException(errorMsg);
        }

        Room room = availableRoom.get();

        // Generate unique reservation number
        int reservationNumber = generateReservationNumber();

        // Create the reservation object
        Reservation reservation = new Reservation(
                reservationNumber, startDate, endDate, payer, room);
        reservations.add(reservation);

        // Update room state to RESERVED (book the room)
        room.bookRoom();

        return reservation;
    }

    /**
     * Generates a unique reservation number based on current reservations.
     */
    private int generateReservationNumber() {
        return reservations.size() + 1;
    }

    /**
     * Cancels an existing reservation and frees up the room.
     */
    public void cancelReservation(int reservationNumber) {
        Reservation reservation = findReservationByNumber(reservationNumber);

        // Remove reservation from list
        reservations.remove(reservation);

        // Free up the room (change state from RESERVED to FREE)
        reservation.getRoom().cancelBooking();
    }

    /**
     * Finds a reservation by its number or throws an exception if not found.
     */
    private Reservation findReservationByNumber(int reservationNumber) {
        return reservations.stream()
                .filter(r -> r.getReservationNumber() == reservationNumber)
                .findFirst()
                .orElseThrow(() -> new HotelException(
                        "Reservation #" + reservationNumber + " not found"));
    }
}
