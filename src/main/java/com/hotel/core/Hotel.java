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

    public boolean available(LocalDate startDate, LocalDate endDate, RoomType roomType) {
        return rooms.stream()
                .filter(room -> room.getRoomType().equals(roomType))
                .anyMatch(room -> isRoomAvailable(room, startDate, endDate));
    }

    private boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        // Simple check: if room is FREE, it's available?
        // No, we need to check against existing reservations for dates properly.
        // But the Room State is "FREE, RESERVED, OCCUPIED".
        // This state pattern is simplistic and doesn't handle future dates well unless "Reserved" means "Reserved for RIGHT NOW".
        // However, the prompt implies "Conflicting Requirements (WP1): flexible... future performance".
        // The diagram has `Reservation` with dates.
        // So `Hotel` must check `reservations` list for overlap.
        // AND check the current state? No, strictly date overlap for future bookings.

        for (Reservation res : reservations) {
            if (res.getRoom().equals(room)) {
                // Check overlap
                if (startDate.isBefore(res.getEndDate()) && endDate.isAfter(res.getStartDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Reservation createReservation(LocalDate startDate, LocalDate endDate, RoomType roomType, ReserverPayer payer) {
        Optional<Room> availableRoom = rooms.stream()
                .filter(room -> room.getRoomType().equals(roomType))
                .filter(room -> isRoomAvailable(room, startDate, endDate))
                .findFirst();

        if (availableRoom.isEmpty()) {
            throw new HotelException("No available room of type " + roomType.getKind());
        }

        Room room = availableRoom.get();
        // Determine reservation number (simple increment or random)
        int resNum = reservations.size() + 1; // Simplistic
        Reservation reservation = new Reservation(resNum, startDate, endDate, payer, room);

        reservations.add(reservation);
        room.makeReservation(); // Updates state to RESERVED.
        // IMPORTANT: Updates state? If reservation is in future, room might be FREE now.
        // But the specific instructions say "maintain relationships... State Management: Prevent illegal object states".
        // And Fig 19 state chart says "makeReservation() -> Reserved".
        // So strictly following the diagram, modifying the state immediately is what is implied,
        // assuming the system might be "reservation for immediate stay" or the state reflects "Has a reservation".
        // BUT "Reserved" usually means "Blocked for a guest arriving soon".
        // If I strictly follow the diagram, I must call `room.makeReservation()`.

        return reservation;
    }

    public void cancelReservation(int reservationNumber) {
        Reservation res = reservations.stream()
                .filter(r -> r.getReservationNumber() == reservationNumber)
                .findFirst()
                .orElseThrow(() -> new HotelException("Reservation not found: " + reservationNumber));

        reservations.remove(res);
        res.getRoom().cancelReservation();
    }
}
