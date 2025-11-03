package com.elevsim;

import java.util.Objects;

public final class CarCall {
    public final int elevatorId;
    public final int destinationFloor;

    public CarCall(int elevatorId, int destinationFloor) {
        this.elevatorId = elevatorId;
        this.destinationFloor = destinationFloor;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof CarCall)) return false;
        CarCall other = (CarCall)o;
        return elevatorId == other.elevatorId && destinationFloor == other.destinationFloor;
    }

    @Override public int hashCode() {
        return Objects.hash(elevatorId, destinationFloor);
    }

    @Override public String toString() {
        return "CarCall{car=" + elevatorId + ", dest=" + destinationFloor + "}";
    }
}
