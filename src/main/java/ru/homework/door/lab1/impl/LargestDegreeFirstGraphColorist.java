package ru.homework.door.lab1.impl;

import java.util.*;
import java.util.stream.Collectors;

public class LargestDegreeFirstGraphColorist extends AbstractGreedyGraphColorist {

    @Override
    protected SequencedCollection<Integer> makeOrdering(final Map<Integer, Set<Integer>> graph) {
        return getDescendingVertices(graph);
    }

    private List<Integer> getDescendingVertices(Map<Integer, Set<Integer>> graph) {
        return graph.entrySet().stream()
                .sorted(Comparator.comparing(entry -> -(entry.getValue().size())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
