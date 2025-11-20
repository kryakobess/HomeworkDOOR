package ru.homework.door.common.structures;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Tabu {
    private final Queue<Integer> tabuQueue = new ArrayDeque<>();
    private final Set<Integer> tabuSet = new HashSet<>();

    public void add(Integer v) {
        tabuQueue.add(v);
        tabuSet.add(v);
    }
}
