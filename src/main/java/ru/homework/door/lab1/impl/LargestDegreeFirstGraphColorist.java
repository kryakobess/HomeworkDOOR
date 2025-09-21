package ru.homework.door.lab1.impl;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.common.structures.Pair;
import ru.homework.door.lab1.GraphColorist;
import ru.homework.door.lab1.dto.ColoringResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LargestDegreeFirstGraphColorist implements GraphColorist {

    @Override
    public ColoringResult colorGraph(Integer verticesCount, List<Edge> edges) {
        return null;
    }


    private List<Integer> getOrderedVertices(Map<Integer, Set<Pair<Integer, Integer>>> graph) {
        return graph.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
