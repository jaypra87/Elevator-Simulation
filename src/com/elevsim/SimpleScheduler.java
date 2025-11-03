package com.elevsim;

import java.util.List;

/**
 * A simple, deterministic SCAN-style scheduler:
 * - Prefer cars already moving toward the hall call in the same direction and that will pass the floor.
 * - Otherwise prefer idle car with minimal distance.
 * - Otherwise choose the car with minimal detour (distance from current pos).
 * Ties break by lower elevator id.
 */

public class SimpleScheduler implements Scheduler {

    @Override
    public int dispatchHallCall(HallCall call, List<Elevator> elevators) {
        int bestId = -1;
        int bestCost = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int cost = costFor(e, call);
            if (cost < bestCost || (cost == bestCost && (bestId == -1 || e.id < bestId))) {
                bestCost = cost;
                bestId = e.id;
            }
        }

        // find the chosen elevator without using a lambda (avoids effectively-final rule)
        Elevator chosen = null;
        for (Elevator ev : elevators) {
            if (ev.id == bestId) { chosen = ev; break; }
        }
        if (chosen == null) throw new IllegalStateException("No elevator with id " + bestId);

        chosen.addStop(call.floor);
        return bestId;
    }

    @Override
    public void dispatchCarCall(CarCall call, List<Elevator> elevators) {
        Elevator e = null;
        for (Elevator ev : elevators) {
            if (ev.id == call.elevatorId) { e = ev; break; }
        }
        if (e == null) throw new IllegalArgumentException("No elevator with id " + call.elevatorId);
        e.addStop(call.destinationFloor);
    }

    private int costFor(Elevator e, HallCall call) {
        int pos = e.getCurrentFloor();
        Direction ed = e.getDirection();
        int distance = Math.abs(call.floor - pos);

        if (e.willPassWhileContinuing(call.floor, call.direction)) {
            return distance;
        }
        if (ed == Direction.IDLE) {
            return distance + 100;
        }
        return distance + 1000;
    }
}

