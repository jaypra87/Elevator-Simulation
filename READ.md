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
  Print the full state of all elevators.

- `tick [n]`  
  Advance the simulation by 'n' ticks (default 1).

- `hall <floor> <up/down>`  
  Create a hall call on a floor with a direction.  
  Example: `hall 5 up`

- `car <elevatorId> <destinationFloor>`  
  Create an inside-the-car destination request.  
  Example: `car 1 12`

- `quit` / `exit`  
  Exit the simulator.
