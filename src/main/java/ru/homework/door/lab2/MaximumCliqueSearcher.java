package ru.homework.door.lab2;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab2.dto.MaximumCliqueResult;

import java.util.List;

public interface MaximumCliqueSearcher {
    MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations);
}
