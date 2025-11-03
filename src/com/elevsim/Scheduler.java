package com.elevsim;

import java.util.List;

public interface Scheduler {
    /**
     * Route a hall call to the best elevator and enqueue the stop.
     * Returns the chosen elevator id.
     */
    int dispatchHallCall(HallCall call, List<Elevator> elevators);

    /**
     * Route a car call (already associated with an elevator).
     */
    void dispatchCarCall(CarCall call, List<Elevator> elevators);
}
