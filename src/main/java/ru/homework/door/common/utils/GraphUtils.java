package ru.homework.door.common.utils;

import ru.homework.door.common.structures.Edge;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphUtils {

    public static Map<Integer, Set<Integer>> buildGraph(Integer vertices, List<Edge> edges) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (var edge : edges) {
            graph.putIfAbsent(edge.from(), new HashSet<>());
            graph.putIfAbsent(edge.to(), new HashSet<>());

            graph.get(edge.from()).add(edge.to());
            graph.get(edge.to()).add(edge.from());
        }

        IntStream.range(1, vertices + 1)
                .forEach(v -> graph.putIfAbsent(v, new HashSet<>()));

        return graph;
    }

    public static Map<Integer, Set<Integer>> copyGraph(final Map<Integer, Set<Integer>> original) {
        Map<Integer, Set<Integer>> copy = new HashMap<>();
        original.forEach((key, value) -> copy.computeIfAbsent(key, k -> new HashSet<>()).addAll(value));
        return copy;
    }

    public static List<Integer> getListOfVertices(Integer verticesCount) {
        return IntStream.range(1, verticesCount + 1)
                .boxed()
                .collect(Collectors.toList());
    }
}
