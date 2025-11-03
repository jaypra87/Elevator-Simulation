# Elevator Simulator (Back-End Code Challenge)

Implements a multi-elevator simulator with a SCAN-style scheduler:
- Multiple cars
- Hall calls (floor + direction)
- Car calls (destination from inside a car)
- Discrete-tick simulation loop with door dwell, movement, and stop servicing
- Deterministic tie-breaks and a simple cost model

## How to run

```bash
# from project root
javac -d out $(find src -name "*.java")
java -cp out com.elevsim.Main

### Commands (Quick Reference)

- `help`  
  Show available commands.

- `status`  
  Print full state of all elevators.

- `tick [n]`  
  Advance the simulation by *n* ticks (default 1).

- `hall &lt;floor&gt; &lt;up|down&gt;`  
  Create a hall call at a floor with a direction.  
  **Example:** `hall 5 up`

- `car &lt;elevatorId&gt; &lt;destinationFloor&gt;`  
  Create an inside-the-car destination request.  
  **Example:** `car 1 12`

- `quit` / `exit`  
  Exit the simulator.
