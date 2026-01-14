package com.hotel.domain;

import java.util.Objects;
import com.hotel.exception.HotelException;

public class Room {
    private final int number;
    private final RoomType roomType;
    private RoomState state;
    private Guest occupant;

    public Room(int number, RoomType roomType) {
        if (roomType == null) {
            throw new IllegalArgumentException("RoomType cannot be null");
        }
        this.number = number;
        this.roomType = roomType;
        this.state = RoomState.FREE;
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
     * Transitions state from FREE to RESERVED.
     */
    public void makeReservation() {
        if (state != RoomState.FREE) {
            throw new HotelException(
                    String.format("Room %d is not free (current state: %s). Cannot make reservation.", number, state));
        }
        this.state = RoomState.RESERVED;
    }

    /**
     * Transitions state from RESERVED to FREE.
     */
    public void cancelReservation() {
        if (state != RoomState.RESERVED) {
            throw new HotelException(String
                    .format("Room %d is not reserved (current state: %s). Cannot cancel reservation.", number, state));
        }
        this.state = RoomState.FREE;
    }

    /**
     * Transitions state from RESERVED to OCCUPIED.
     * 
     * @param guest The guest checking in.
     */
    public void checkInGuest(Guest guest) {
        if (state != RoomState.RESERVED) {
            throw new HotelException(
                    String.format("Room %d must be reserved before check-in (current state: %s).", number, state));
        }
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }
        this.state = RoomState.OCCUPIED;
        this.occupant = guest;
    }

    /**
     * Transitions state from OCCUPIED to FREE.
     */
    public void checkOutGuest() {
        if (state != RoomState.OCCUPIED) {
            throw new HotelException(
                    String.format("Room %d is not occupied (current state: %s). Cannot check out.", number, state));
        }
        this.state = RoomState.FREE;
        this.occupant = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Room room = (Room) o;
        return number == room.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
