package com.elevsim;

import java.util.*;

/** Owns the building state and advances ticks. */
public class Simulator {
    private final int minFloor;
    private final int maxFloor;
    private final List<Elevator> elevators;
    private final Scheduler scheduler;

    public Simulator(int numElevators, int minFloor, int maxFloor, int startFloor, Scheduler scheduler) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.scheduler = scheduler;
        this.elevators = new ArrayList<>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i, startFloor, minFloor, maxFloor));
        }
    }

    public List<Elevator> getElevators() { return elevators; }

    public boolean validFloor(int f) { return f >= minFloor && f <= maxFloor; }

    public int hallCall(int floor, Direction dir) {
        if (!validFloor(floor)) throw new IllegalArgumentException("Invalid floor: " + floor);
        return scheduler.dispatchHallCall(new HallCall(floor, dir), elevators);
    }

    public void carCall(int elevatorId, int destination) {
        if (!validFloor(destination)) throw new IllegalArgumentException("Invalid floor: " + destination);
        scheduler.dispatchCarCall(new CarCall(elevatorId, destination), elevators);
    }

    /** Advance the world by 1 tick; returns map of carId -> floors served this tick. */
    public Map<Integer, List<Integer>> tick() {
        Map<Integer, List<Integer>> served = new LinkedHashMap<>();
        for (Elevator e : elevators) {
            served.put(e.id, e.tick());
        }
        return served;
    }

    public String status() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Building floors: [%d..%d]\n", minFloor, maxFloor));
        for (Elevator e : elevators) {
            sb.append("  ").append(e.debug()).append("\n");
        }
        return sb.toString();
    }
}
