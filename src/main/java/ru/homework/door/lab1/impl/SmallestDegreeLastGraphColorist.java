package ru.homework.door.lab1.impl;

import ru.homework.door.common.structures.DecreaseKeyBinaryHeap;

import java.util.*;

public class SmallestDegreeLastGraphColorist extends AbstractGreedyGraphColorist {

    @Override
    protected SequencedCollection<Integer> makeOrdering(final Map<Integer, Set<Integer>> graph) {
        var copyGraph = Map.copyOf(graph);
        Deque<Integer> smallestDegreeLastOrdering = new ArrayDeque<>();

        DecreaseKeyBinaryHeap<Integer> priorityQueue = new DecreaseKeyBinaryHeap<>();
        graph.forEach((vertex, neighbors) -> priorityQueue.add(vertex, neighbors.size()));

        while (!priorityQueue.isEmpty()) {
            Integer minDegreeVertex = priorityQueue.getMinAndRemove();
            removeVertex(minDegreeVertex, copyGraph, priorityQueue);
            smallestDegreeLastOrdering.addFirst(minDegreeVertex);
        }

        return smallestDegreeLastOrdering;
    }

    private void removeVertex(
            Integer vertex,
            Map<Integer, Set<Integer>> graph,
            DecreaseKeyBinaryHeap<Integer> priorityQueue
    ) {
        for (var neighbor : graph.get(vertex)) {
            graph.get(neighbor).remove(vertex);
            priorityQueue.decreaseKey(neighbor);
        }
        graph.remove(vertex);
    }

}
