package com.elevsim;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A single elevator car with SCAN-style stop sets in each direction.
 * Movement is one floor per tick. Doors dwell for DWELL_TICKS when opening.
 */
public class Elevator {
    public final int id;
    private final int minFloor, maxFloor;

    // state
    private int currentFloor;
    private Direction direction = Direction.IDLE;
    private DoorState doorState = DoorState.CLOSED;
    private int dwellRemaining = 0;

    // stops are represented as ordered sets for each direction
    private final TreeSet<Integer> upStops = new TreeSet<>();
    private final TreeSet<Integer> downStops = new TreeSet<>(Comparator.reverseOrder());

    // timing constants (ticks)
    public static final int DWELL_TICKS = 2; // how long doors stay open when stopping

    public Elevator(int id, int startFloor, int minFloor, int maxFloor) {
        this.id = id;
        this.currentFloor = startFloor;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    public int getCurrentFloor() { return currentFloor; }
    public Direction getDirection() { return direction; }
    public DoorState getDoorState() { return doorState; }

    /** Add a target floor in a SCAN-friendly way. */
    public void addStop(int floor) {
        if (floor < minFloor || floor > maxFloor || floor == currentFloor) return;
        if (floor > currentFloor) upStops.add(floor);
        else if (floor < currentFloor) downStops.add(floor);
        // if idle, choose initial movement
        if (direction == Direction.IDLE) {
            direction = !upStops.isEmpty() ? Direction.UP : (!downStops.isEmpty() ? Direction.DOWN : Direction.IDLE);
        }
    }

    /** True if this car will pass 'floor' while continuing in 'dir' without reversing first. */
    public boolean willPassWhileContinuing(int floor, Direction dir) {
        if (dir == Direction.UP && direction == Direction.UP) {
            int highest = upStops.isEmpty() ? maxFloor : upStops.last();
            return floor >= currentFloor && floor <= highest;
        }
        if (dir == Direction.DOWN && direction == Direction.DOWN) {
            int lowest = downStops.isEmpty() ? minFloor : downStops.last();
            return floor <= currentFloor && floor >= lowest;
        }
        return false;
    }

    /** Nearest upcoming stop in current direction, else the opposite direction. */
    private OptionalInt nextStop() {
        if (direction == Direction.UP && !upStops.isEmpty()) return OptionalInt.of(upStops.first());
        if (direction == Direction.DOWN && !downStops.isEmpty()) return OptionalInt.of(downStops.first());
        if (!upStops.isEmpty())  { direction = Direction.UP;  return OptionalInt.of(upStops.first()); }
        if (!downStops.isEmpty()){ direction = Direction.DOWN; return OptionalInt.of(downStops.first()); }
        direction = Direction.IDLE;
        return OptionalInt.empty();
    }

    /** Advance one tick. Returns a list of floors served in this tick (0 or 1 element). */
    public List<Integer> tick() {
        List<Integer> served = new ArrayList<>(1);

        // If doors are open, dwell
        if (doorState == DoorState.OPEN) {
            if (dwellRemaining > 0) {
                dwellRemaining--;
                return served;
            } else {
                doorState = DoorState.CLOSED;
            }
        }

        // If at a scheduled stop, open and serve
        boolean stoppingHere = (direction == Direction.UP && upStops.contains(currentFloor))
                             || (direction == Direction.DOWN && downStops.contains(currentFloor));
        if (stoppingHere) {
            upStops.remove(currentFloor);
            downStops.remove(currentFloor);
            doorState = DoorState.OPEN;
            dwellRemaining = DWELL_TICKS;
            served.add(currentFloor);
            // If no more stops in this direction, we may flip later via nextStop()
            return served;
        }

        // Move toward next stop if exists
        OptionalInt target = nextStop();
        if (target.isPresent() && direction != Direction.IDLE) {
            int t = target.getAsInt();
            if (t > currentFloor) currentFloor++;
            else if (t < currentFloor) currentFloor--;
            // If we just arrived, next tick will open doors
        } else {
            // no targets
            direction = Direction.IDLE;
        }

        return served;
    }

    /** For visualization / debugging */
    public String debug() {
        String up = upStops.stream().map(Object::toString).collect(Collectors.joining(","));
        String dn = downStops.stream().map(Object::toString).collect(Collectors.joining(","));
        return String.format(
            "Elevator[%d] f=%d dir=%s door=%s dwell=%d up=[%s] dn=[%s]",
            id, currentFloor, direction, doorState, dwellRemaining, up, dn
        );
    }
}
