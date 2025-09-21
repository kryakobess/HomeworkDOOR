package ru.homework.door.lab1.impl;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab1.GraphColorist;
import ru.homework.door.lab1.dto.ColoringResult;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractGreedyGraphColorist implements GraphColorist {

    @Override
    public ColoringResult colorGraph(Integer verticesCount, List<Edge> edges) {
        Map<Integer, Set<Integer>> graph = getGraph(edges);

        SequencedCollection<Integer> smallestDegreeLastOrdering = makeOrdering(graph);
        Map<Integer, Integer> vertexColor = colorVertices(smallestDegreeLastOrdering, graph);

        return new ColoringResult(getVerticesGroupedByColors(vertexColor));
    }

    protected abstract SequencedCollection<Integer> makeOrdering(final Map<Integer, Set<Integer>> graph);

    private Map<Integer, Integer> colorVertices(
            SequencedCollection<Integer> ordering,
            final Map<Integer, Set<Integer>> graph
    ) {
        Map<Integer, Integer> vertexColor = new HashMap<>();
        for (var vertex : ordering) {
            Set<Integer> neighborColors = new HashSet<>();
            for (var neighbor : graph.get(vertex)) {
                if (vertexColor.containsKey(neighbor)) {
                    neighborColors.add(vertexColor.get(neighbor));
                }
                int availableColor = getAvailableColor(neighborColors);
                vertexColor.put(vertex, availableColor);
            }
        }
        return vertexColor;
    }

    private Integer getAvailableColor(Set<Integer> neighborColors) {
        int color = 0;
        while (neighborColors.contains(color)) {
            color++;
        }
        return color;
    }

    private Map<Integer, Set<Integer>> getGraph(List<Edge> edges) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (var edge : edges) {
            graph.putIfAbsent(edge.from(), new HashSet<>());
            graph.putIfAbsent(edge.to(), new HashSet<>());

            graph.get(edge.from()).add(edge.to());
            graph.get(edge.to()).add(edge.from());
        }
        return graph;
    }

    private List<List<Integer>> getVerticesGroupedByColors(Map<Integer, Integer> vertexColor) {
        return new ArrayList<>(
                vertexColor.entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Map.Entry::getValue,
                                Collectors.mapping(Map.Entry::getKey, Collectors.toList()))
                        )
                        .values()
        );
    }
}
