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

public class Hotel {
    private final String name;
    private final List<Room> rooms;
    private final List<Reservation> reservations;

    public Hotel(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hotel name cannot be empty");
        }
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

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
     * Checks if a room of the given type is available for the given dates.
     * Availability requires checking both date overlaps and current room state.
     */
    public boolean available(LocalDate startDate, LocalDate endDate, RoomType roomType) {
        return rooms.stream()
                .filter(room -> room.getRoomType().equals(roomType))
                .anyMatch(room -> isRoomAvailable(room, startDate, endDate));
    }

    private boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        // Check 1: Overlap with existing reservations for this room
        for (Reservation res : reservations) {
            if (res.getRoom().equals(room)) {
                // If requested dates overlap with an existing reservation
                // Standard overlap condition: (StartA < EndB) and (EndA > StartB)
                // Assuming start date is inclusive and end date is exclusive (or checkout
                // morning)

                // If the user wants STRICT day overlap:
                // [10, 12] overlaps [11, 13]? Yes.
                // 10 < 13 && 12 > 11. True.
                if (startDate.isBefore(res.getEndDate()) && endDate.isAfter(res.getStartDate())) {
                    return false;
                }
            }
        }

        // Check 2: Current Room State
        // According to the strict UML state chart (Fig 19), makeReservation can only be
        // called if state is FREE.
        // If the room is already RESERVED or OCCUPIED, we cannot "reserve" it again in
        // the simple state machine.
        // This limits the system to one active transaction per room, but aligns with
        // strict UML instructions.
        if (room.getState() != RoomState.FREE) {
            return false;
        }

        return true;
    }

    public Reservation createReservation(LocalDate startDate, LocalDate endDate, RoomType roomType,
            ReserverPayer payer) {
        Optional<Room> availableRoom = rooms.stream()
                .filter(room -> room.getRoomType().equals(roomType))
                .filter(room -> isRoomAvailable(room, startDate, endDate))
                .findFirst();

        if (availableRoom.isEmpty()) {
            throw new HotelException("No available room of type " + roomType.getKind() + " for the given dates.");
        }

        Room room = availableRoom.get();
        // Generate a reservation number (1-based index)
        int resNum = reservations.size() + 1;

        Reservation reservation = new Reservation(resNum, startDate, endDate, payer, room);
        reservations.add(reservation);

        // Update Room State (This is the critical strict UML step)
        // This will transition the room from FREE to RESERVED.
        room.makeReservation();

        return reservation;
    }

    public void cancelReservation(int reservationNumber) {
        Reservation res = reservations.stream()
                .filter(r -> r.getReservationNumber() == reservationNumber)
                .findFirst()
                .orElseThrow(() -> new HotelException("Reservation #" + reservationNumber + " not found."));

        reservations.remove(res);
        res.getRoom().cancelReservation();
    }
}
