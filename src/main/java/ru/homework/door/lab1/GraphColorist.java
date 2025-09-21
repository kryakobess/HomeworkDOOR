package ru.homework.door.lab1;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab1.dto.ColoringResult;

import java.util.List;

public interface GraphColorist {
    ColoringResult colorGraph(Integer verticesCount, List<Edge> edges);
}
