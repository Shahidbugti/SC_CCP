package com.hotel.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a hotel room reservation with dates and payment information.
 */
public class Reservation {
    private final int reservationNumber;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ReserverPayer payer;
    private final Room room;

    public Reservation(int reservationNumber, LocalDate startDate, LocalDate endDate,
            ReserverPayer payer, Room room) {
        validateDates(startDate, endDate);
        validatePayer(payer);
        validateRoom(room);

        this.reservationNumber = reservationNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payer = payer;
        this.room = room;
    }

    /**
     * Validates that dates are not null and end date is after start date.
     */
    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Reservation dates cannot be null");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }
    }

    /**
     * Validates that payer information is provided.
     */
    private void validatePayer(ReserverPayer payerInfo) {
        if (payerInfo == null) {
            throw new IllegalArgumentException("Payer information is required");
        }
    }

    /**
     * Validates that room is assigned.
     */
    private void validateRoom(Room assignedRoom) {
        if (assignedRoom == null) {
            throw new IllegalArgumentException("Room must be assigned to reservation");
        }
    }

    /**
     * Calculates the number of nights for this reservation.
     */
    public long getDurationInNights() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Checks if this reservation is currently active (today is between start and
     * end dates).
     */
    public boolean isActiveOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ReserverPayer getPayer() {
        return payer;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Reservation otherReservation = (Reservation) other;
        return reservationNumber == otherReservation.reservationNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationNumber);
    }
}
