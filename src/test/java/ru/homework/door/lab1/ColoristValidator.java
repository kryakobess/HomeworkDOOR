package ru.homework.door.lab1;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab1.dto.ColoringResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ColoristValidator {

    public static void validateColoring(int verticesCount, List<Edge> edges, ColoringResult result, String description) {
        // Создаем массив цветов для каждой вершины
        int[] vertexColors = new int[verticesCount + 1];
        Arrays.fill(vertexColors, -1);

        for (int color = 0; color < result.verticesGroupedByColor().size(); color++) {
            for (int vertex : result.verticesGroupedByColor().get(color)) {
                assertTrue(vertex > 0 && vertex <= verticesCount,
                        description + ": Vertex index out of bounds");
                assertEquals(-1, vertexColors[vertex],
                        description + ": Vertex " + vertex + " colored multiple times");
                vertexColors[vertex] = color;
            }
        }

        // Проверяем, что все вершины раскрашены
        for (int i = 1; i < verticesCount + 1; i++) {
            assertNotEquals(-1, vertexColors[i],
                    description + ": Vertex " + i + " not colored");
        }

        // Проверяем, что смежные вершины имеют разные цвета
        for (Edge edge : edges) {
            int from = edge.from();
            int to = edge.to();
            assertNotEquals(vertexColors[from], vertexColors[to],
                    description + ": Adjacent vertices " + from + " and " + to + " have same color");
        }
    }

}
