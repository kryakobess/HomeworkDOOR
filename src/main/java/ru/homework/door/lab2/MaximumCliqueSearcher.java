package ru.homework.door.lab2;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab2.dto.MaximumCliqueResult;
import ru.homework.door.lab3.CliqueSearchImprover;

import java.util.List;

public interface MaximumCliqueSearcher {
    MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations, CliqueSearchImprover improver);
    MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations);
}
