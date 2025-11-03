package com.elevsim;

import java.util.Objects;

public final class HallCall {
    public final int floor;
    public final Direction direction;

    public HallCall(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof HallCall)) return false;
        HallCall other = (HallCall)o;
        return floor == other.floor && direction == other.direction;
    }

    @Override public int hashCode() {
        return Objects.hash(floor, direction);
    }

    @Override public String toString() {
        return "HallCall{floor=" + floor + ", dir=" + direction + "}";
    }
}
