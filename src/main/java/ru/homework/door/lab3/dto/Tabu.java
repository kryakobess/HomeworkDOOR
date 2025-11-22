package ru.homework.door.lab3.dto;

import java.util.*;

public class Tabu {
    private final Map<Integer, Integer> tabuMap = new HashMap<>();
    private final int coolDown;

    public Tabu(int coolDown) {
        this.coolDown = coolDown;
    }

    public void add(Integer v) {
        tabuMap.put(v, coolDown);
    }

    public void expire() {
        for (var entry : tabuMap.entrySet()) {
            entry.setValue(entry.getValue() - 1);
        }
        tabuMap.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }

    public boolean contains(Integer v) {
        return tabuMap.containsKey(v);
    }
}
