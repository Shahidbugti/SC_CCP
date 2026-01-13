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

    public void makeReservation() {
        if (state != RoomState.FREE) {
            throw new HotelException("Cannot reserve room " + number + "; implementation state is " + state);
        }
        // In a real system, we'd check dates. Here we follow the simple state chart.
        this.state = RoomState.RESERVED;
    }

    public void cancelReservation() {
        if (state != RoomState.RESERVED) {
            throw new HotelException("Cannot cancel reservation for room " + number + "; is not reserved");
        }
        this.state = RoomState.FREE;
    }

    public void checkInGuest(Guest guest) {
        if (state != RoomState.RESERVED && state != RoomState.FREE) {
             // Depending on policy, maybe walk-ins are allowed (Free -> Occupied)
             // But diagram state chart has Free -> Reserved -> Occupied (triangle)
             // Actually Fig 19 shows: Free -> Reserved -> Occupied.
             // And Occupied -> Free.
             // Wait, the arrow checkInGuest is from Reserved to Occupied.
             throw new HotelException("Room " + number + " must be reserved before check-in or is already occupied");
        }
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }
        this.state = RoomState.OCCUPIED;
        this.occupant = guest;
    }

    public void checkOutGuest() {
        if (state != RoomState.OCCUPIED) {
            throw new HotelException("Cannot check out from room " + number + "; implementation is not occupied");
        }
        this.state = RoomState.FREE;
        this.occupant = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        Room room = (Room) o;
        return number == room.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
