package com.hotel.domain;

import java.util.Objects;
import com.hotel.exception.HotelException;

/**
 * Represents a hotel room with state management for bookings and occupancy.
 * Room states: FREE -> RESERVED -> OCCUPIED -> FREE
 */
public class Room {
    private final int number;
    private final RoomType roomType;
    private RoomState state;
    private Guest occupant;

    public Room(int number, RoomType roomType) {
        validateRoomType(roomType);

        this.number = number;
        this.roomType = roomType;
        this.state = RoomState.FREE;
    }

    /**
     * Validates that room type is not null.
     */
    private void validateRoomType(RoomType type) {
        if (type == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
    }

    public int getNumber() {
        return number;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomState getState() {
        return state;
    }

    public Guest getOccupant() {
        return occupant;
    }

    /**
     * Checks if room is currently free (available for booking).
     */
    public boolean isFree() {
        return state == RoomState.FREE;
    }

    /**
     * Checks if room is currently booked/reserved.
     */
    public boolean isBooked() {
        return state == RoomState.RESERVED;
    }

    /**
     * Checks if room is currently occupied by a guest.
     */
    public boolean isOccupied() {
        return state == RoomState.OCCUPIED;
    }

    /**
     * Books the room (transitions from FREE to RESERVED).
     * This is called when a customer makes a reservation.
     */
    public void bookRoom() {
        if (!isFree()) {
            String errorMsg = String.format(
                    "Cannot book room %d - current state is %s (must be FREE)",
                    number, state);
            throw new HotelException(errorMsg);
        }
        this.state = RoomState.RESERVED;
    }

    /**
     * Cancels a booking (transitions from RESERVED to FREE).
     * This is called when a customer cancels their reservation.
     */
    public void cancelBooking() {
        if (!isBooked()) {
            String errorMsg = String.format(
                    "Cannot cancel room %d - current state is %s (must be RESERVED)",
                    number, state);
            throw new HotelException(errorMsg);
        }
        this.state = RoomState.FREE;
    }

    /**
     * Checks in a guest (transitions from RESERVED to OCCUPIED).
     * The guest must arrive with a valid reservation.
     * 
     * @param guest The guest checking in
     */
    public void checkInGuest(Guest guest) {
        validateGuestForCheckIn(guest);

        this.state = RoomState.OCCUPIED;
        this.occupant = guest;
    }

    /**
     * Validates that room is reserved and guest is not null before check-in.
     */
    private void validateGuestForCheckIn(Guest guest) {
        if (!isBooked()) {
            String errorMsg = String.format(
                    "Cannot check in to room %d - must have a reservation first (current state: %s)",
                    number, state);
            throw new HotelException(errorMsg);
        }
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null for check-in");
        }
    }

    /**
     * Checks out the current guest (transitions from OCCUPIED to FREE).
     * This is called when a guest leaves the hotel.
     */
    public void checkOutGuest() {
        if (!isOccupied()) {
            String errorMsg = String.format(
                    "Cannot check out from room %d - no guest is currently staying (current state: %s)",
                    number, state);
            throw new HotelException(errorMsg);
        }
        this.state = RoomState.FREE;
        this.occupant = null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Room otherRoom = (Room) other;
        return number == otherRoom.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
