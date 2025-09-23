package ru.homework.door.lab1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab1.dto.ColoringResult;
import ru.homework.door.lab1.impl.SmallestDegreeLastGraphColorist;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.homework.door.lab1.ColoristValidator.validateColoring;

class SmallestDegreeLastGraphColoristParameterizedTest {

    private final GraphColorist colorist = new SmallestDegreeLastGraphColorist();

    @ParameterizedTest
    @MethodSource("provideBasicGraphs")
    void testBasicGraphs(String description, int verticesCount, List<Edge> edges, int expectedMaxColors) {
        ColoringResult result = colorist.colorGraph(verticesCount, edges);

        assertNotNull(result, description + ": Result should not be null");
        assertNotNull(result.verticesGroupedByColor(), description + ": Colors list should not be null");

        // Проверяем, что количество цветов не превышает ожидаемого
        assertTrue(result.verticesGroupedByColor().size() <= expectedMaxColors,
                description + ": Too many colors used");

        // Проверяем корректность раскраски
        validateColoring(verticesCount, edges, result, description);
    }

    private static Stream<Arguments> provideBasicGraphs() {
        return Stream.of(
                Arguments.of("Empty graph", 0, List.of(), 0),
                Arguments.of("Single vertex", 1, List.of(), 1),
                Arguments.of("Two independent vertices", 2, List.of(), 1),
                Arguments.of("Two connected vertices", 2, List.of(new Edge(1, 2)), 2),
                Arguments.of("Triangle (K3)", 3, List.of(
                        new Edge(1, 2), new Edge(2, 3), new Edge(3, 1)
                ), 3),
                Arguments.of("Path graph of 3 vertices", 3, List.of(
                        new Edge(1, 2), new Edge(2, 3)
                ), 2),
                Arguments.of("Square (C4)", 4, List.of(
                        new Edge(1, 2), new Edge(2, 3), new Edge(3, 4), new Edge(4, 1)
                ), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCompleteGraphs")
    void testCompleteGraphs(String description, int n, int expectedColors) {
        List<Edge> edges = generateCompleteGraph(n);
        ColoringResult result = colorist.colorGraph(n, edges);

        assertNotNull(result, description + ": Result should not be null");
        assertEquals(expectedColors, result.verticesGroupedByColor().size(),
                description + ": Complete graph should use exactly n colors");
        validateColoring(n, edges, result, description);
    }

    private static Stream<Arguments> provideCompleteGraphs() {
        return Stream.of(
                Arguments.of("K1", 1, 1),
                Arguments.of("K2", 2, 2),
                Arguments.of("K3", 3, 3),
                Arguments.of("K4", 4, 4),
                Arguments.of("K5", 5, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBipartiteGraphs")
    void testBipartiteGraphs(String description, int verticesCount, List<Edge> edges) {
        ColoringResult result = colorist.colorGraph(verticesCount, edges);

        assertNotNull(result, description + ": Result should not be null");
        // Двудольные графы должны раскрашиваться в 2 цвета
        assertTrue(result.verticesGroupedByColor().size() <= 2,
                description + ": Bipartite graph should use at most 2 colors");
        validateColoring(verticesCount, edges, result, description);
    }

    private static Stream<Arguments> provideBipartiteGraphs() {
        return Stream.of(
                Arguments.of("Simple bipartite", 4, List.of(
                        new Edge(1, 3), new Edge(1, 4), new Edge(2, 3), new Edge(2, 4)
                )),
                Arguments.of("Star graph K1,4", 5, List.of(
                        new Edge(1, 2), new Edge(1, 3), new Edge(1, 4), new Edge(1, 5)
                )),
                Arguments.of("Complete bipartite K2,3", 5, List.of(
                        new Edge(1, 4), new Edge(1, 5), new Edge(2, 4), new Edge(2, 5), new Edge(3, 4), new Edge(3, 5)
                )),
                Arguments.of("Even cycle C6", 6, List.of(
                        new Edge(1, 2), new Edge(2, 3), new Edge(3, 4),
                        new Edge(4, 5), new Edge(5, 6), new Edge(6, 1)
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCycleGraphs")
    void testCycleGraphs(String description, int cycleLength, int expectedColors) {
        List<Edge> edges = generateCycleGraph(cycleLength);
        ColoringResult result = colorist.colorGraph(cycleLength, edges);

        assertNotNull(result, description + ": Result should not be null");
        assertEquals(expectedColors, result.verticesGroupedByColor().size(),
                description + ": Cycle graph should use expected number of colors");
        validateColoring(cycleLength, edges, result, description);
    }

    private static Stream<Arguments> provideCycleGraphs() {
        return Stream.of(
                Arguments.of("C3 (triangle)", 3, 3),
                Arguments.of("C4 (square)", 4, 2),
                Arguments.of("C5 (pentagon)", 5, 3),
                Arguments.of("C6 (hexagon)", 6, 2),
                Arguments.of("C7 (heptagon)", 7, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideComplexGraphs")
    void testComplexGraphs(String description, int verticesCount, List<Edge> edges, int expectedMaxColors) {
        ColoringResult result = colorist.colorGraph(verticesCount, edges);

        assertNotNull(result, description + ": Result should not be null");
        assertTrue(result.verticesGroupedByColor().size() <= expectedMaxColors,
                description + ": Should not exceed expected maximum colors");
        validateColoring(verticesCount, edges, result, description);

        // Дополнительная проверка: все вершины должны быть раскрашены
        int totalVertices = result.verticesGroupedByColor().stream()
                .mapToInt(List::size)
                .sum();
        assertEquals(verticesCount, totalVertices,
                description + ": All vertices should be colored");
    }

    private static Stream<Arguments> provideComplexGraphs() {
        return Stream.of(
                Arguments.of("Wheel graph W4", 5, List.of(
                        new Edge(1, 2), new Edge(1, 3), new Edge(1, 4), new Edge(1, 5),
                        new Edge(2, 3), new Edge(3, 4), new Edge(4, 5), new Edge(5, 2)
                ), 4),
                Arguments.of("Petersen graph", 10, List.of(
                        // Внешний цикл
                        new Edge(1, 2), new Edge(2, 3), new Edge(3, 4), new Edge(4, 5), new Edge(5, 1),
                        // Внутренний цикл
                        new Edge(6, 8), new Edge(8, 10), new Edge(10, 7), new Edge(7, 9), new Edge(9, 6),
                        // Соединения
                        new Edge(1, 6), new Edge(2, 7), new Edge(3, 8), new Edge(4, 9), new Edge(5, 10)
                ), 3), // Петерсен граф 3-раскрашиваем
                Arguments.of("Tree graph", 7, List.of(
                        new Edge(1, 2), new Edge(1, 3), new Edge(2, 4), new Edge(2, 5),
                        new Edge(3, 6), new Edge(3, 7)
                ), 2) // Деревья 2-раскрашиваемы
        );
    }

    private List<Edge> generateCompleteGraph(int n) {
        return java.util.stream.IntStream.range(1, n + 1)
                .boxed()
                .flatMap(i -> java.util.stream.IntStream.range(i + 1, n + 1)
                        .mapToObj(j -> new Edge(i, j)))
                .toList();
    }

    private List<Edge> generateCycleGraph(int n) {
        return java.util.stream.IntStream.range(1, n + 1)
                .mapToObj(i -> new Edge(i, i % n + 1))
                .toList();
    }
}