package ru.homework.door.lab1.impl;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab1.GraphColorist;
import ru.homework.door.lab1.dto.ColoringResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.homework.door.common.utils.GraphUtils.buildGraph;

public abstract class AbstractGreedyGraphColorist implements GraphColorist {

    private static final int FIRST_COLOR = 1;

    @Override
    public ColoringResult colorGraph(Integer verticesCount, List<Edge> edges) {
        Map<Integer, Set<Integer>> graph = buildGraph(verticesCount, edges);

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
            }
            int availableColor = getAvailableColor(neighborColors);
            vertexColor.put(vertex, availableColor);
        }
        return vertexColor;
    }

    private Integer getAvailableColor(Set<Integer> neighborColors) {
        int color = FIRST_COLOR;
        while (neighborColors.contains(color)) {
            color++;
        }
        return color;
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
