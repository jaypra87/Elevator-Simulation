package com.elevsim;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Minimal CLI to drive the simulator interactively in VS Code's terminal.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // Config: tweak as desired
        int minFloor = 0;
        int maxFloor = 15;
        int startFloor = 0;
        int numElevators = 3;

        Simulator sim = new Simulator(numElevators, minFloor, maxFloor, startFloor, new SimpleScheduler());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Elevator Simulator");
        System.out.println("Type 'help' for commands.\n");
        System.out.print(sim.status());

        while (true) {
            System.out.print("> ");
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase(Locale.ROOT);

            try {
                switch (cmd) {
                    case "help":
                        printHelp();
                        break;
                    case "status":
                        System.out.print(sim.status());
                        break;
                    case "quit":
                    case "exit":
                        return;
                    case "tick": {
                        int n = (parts.length >= 2) ? Integer.parseInt(parts[1]) : 1;
                        for (int i = 0; i < n; i++) {
                            Map<Integer, List<Integer>> served = sim.tick();
                            System.out.println("Tick " + (i + 1) + ": " + served);
                        }
                        break;
                    }
                    case "hall": {
                        // hall <floor> <up|down>
                        if (parts.length < 3) { System.out.println("usage: hall <floor> <up|down>"); break; }
                        int floor = Integer.parseInt(parts[1]);
                        Direction dir = parseDirection(parts[2]);
                        int chosen = sim.hallCall(floor, dir);
                        System.out.println("Assigned to elevator " + chosen);
                        break;
                    }
                    case "car": {
                        // car <elevatorId> <destFloor>
                        if (parts.length < 3) { System.out.println("usage: car <elevatorId> <destFloor>"); break; }
                        int id = Integer.parseInt(parts[1]);
                        int dest = Integer.parseInt(parts[2]);
                        sim.carCall(id, dest);
                        System.out.println("Enqueued destination " + dest + " on elevator " + id);
                        break;
                    }
                    default:
                        System.out.println("Unknown command: " + cmd);
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("""
            Commands:
              status
              tick [n]
              hall <floor> <up|down>
              car <elevatorId> <destinationFloor>
              help
              quit
            """);
    }

    private static Direction parseDirection(String s) {
        String v = s.toLowerCase(Locale.ROOT);
        if (v.equals("up")) return Direction.UP;
        if (v.equals("down")) return Direction.DOWN;
        throw new IllegalArgumentException("direction must be 'up' or 'down'");
    }
}
