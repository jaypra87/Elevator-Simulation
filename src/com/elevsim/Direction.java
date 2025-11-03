package com.elevsim;

public enum Direction {
  UP, DOWN, IDLE;
  
  public static Direction fromFloors(int from, int to) {
    if (to > from) {
      return UP;
    }

    if (to < from) {
      return DOWN;
    }
    
    return IDLE;
  }
}
