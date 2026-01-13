package com.hotel.domain;

import java.util.Objects;

public class RoomType {
    private final RoomKind kind;
    private final Money cost;

    public RoomType(RoomKind kind, Money cost) {
        if (kind == null) {
            throw new IllegalArgumentException("RoomKind must be non-null");
        }
        if (cost == null) {
            throw new IllegalArgumentException("Cost must be non-null");
        }
        this.kind = kind;
        this.cost = cost;
    }

    public RoomKind getKind() {
        return kind;
    }

    public Money getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        RoomType roomType = (RoomType) o;
        return kind == roomType.kind && Objects.equals(cost, roomType.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, cost);
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "kind=" + kind +
                ", cost=" + cost +
                '}';
    }
}
