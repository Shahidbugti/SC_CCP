package com.hotel.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final int reservationNumber;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ReserverPayer payer;
    private final Room room;

    public Reservation(int reservationNumber, LocalDate startDate, LocalDate endDate, ReserverPayer payer, Room room) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (payer == null) {
            throw new IllegalArgumentException("Payer cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.reservationNumber = reservationNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payer = payer;
        this.room = room;
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
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        Reservation that = (Reservation) o;
        return reservationNumber == that.reservationNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationNumber);
    }
}
