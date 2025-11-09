package ru.homework.door.common;

import ru.homework.door.common.structures.Edge;

import java.util.List;

public class GraphData {
    public final int verticesCount;
    public final int edgesCount;
    public final List<Edge> edges;

    public GraphData(int verticesCount, int edgesCount, List<Edge> edges) {
        this.verticesCount = verticesCount;
        this.edgesCount = edgesCount;
        this.edges = edges;
    }
}