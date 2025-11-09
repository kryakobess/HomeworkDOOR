package ru.homework.door.lab2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.homework.door.common.GraphData;
import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab2.dto.MaximumCliqueResult;
import ru.homework.door.lab2.impl.GreedyRandomizedCliqueSearcher;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.homework.door.common.GraphInputParser.parseGraphFile;

class GreedyRandomizedCliqueSearcherTest {

    private MaximumCliqueSearcher cliqueSearcher = new GreedyRandomizedCliqueSearcher();

    private static Stream<Arguments> provideFilesToTest() {
        return Stream.of(
                Arguments.of("brock200_1.clq", 20),
                Arguments.of("brock200_2.clq", 10),
                Arguments.of("brock200_3.clq", 14),
                Arguments.of("brock200_4.clq", 16),
                Arguments.of("brock400_1.clq", 24),
                Arguments.of("brock400_2.clq", 25),
                Arguments.of("brock400_3.clq", 24),
                Arguments.of("brock400_4.clq", 24),
                Arguments.of("C125.9.clq", 34),
                Arguments.of("gen200_p0.9_44.clq", 40),
                Arguments.of("gen200_p0.9_55.clq", 48),
                Arguments.of("hamming8-4.clq", 16),
                Arguments.of("johnson16-2-4.clq", 8),
                Arguments.of("johnson8-2-4.clq", 4),
                Arguments.of("keller4.clq", 11),
                Arguments.of("MANN_a27.clq", 126),
                Arguments.of("MANN_a9.clq", 16),
                Arguments.of("p_hat1000-1.clq", 10),
                Arguments.of("p_hat1000-2.clq", 46),
                Arguments.of("p_hat1500-1.clq", 11),
                Arguments.of("p_hat300-3.clq", 34),
                Arguments.of("p_hat500-3.clq", 49),
                Arguments.of("san1000.clq", 10),
                Arguments.of("sanr200_0.9.clq", 41),
                Arguments.of("sanr400_0.7.clq", 21)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilesToTest")
    void testDataFromFile(String fileName, int expectedMinCliqueSize) {
        GraphData graphData = parseGraphFile(Paths.get("src", "main", "resources", "maximum_clique", fileName).toFile());

        long startTime = System.nanoTime();

        MaximumCliqueResult result = cliqueSearcher.findMaxClique(
                graphData.verticesCount,
                graphData.edges,
                0.5,  // alpha параметр
                100  // maxIterations параметр
        );

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf("Время выполнения = %.5f сек\n", duration / 1_000_000_000.0);

        assertNotNull(result, "Результат не должен быть null");
        assertNotNull(result.clique(), "Клика не должна быть null");
        assertFalse(result.clique().isEmpty(), "Клика не должна быть пустой");

        validateClique(graphData.edges, result.clique(), fileName);
        System.out.println("Найденная клика: " + result.clique());
        System.out.printf("Размер клики для %s должен быть не меньше %d. Полученный размер: %d.%n",
                fileName, expectedMinCliqueSize, result.clique().size());

        assertTrue(
                result.clique().size() >= expectedMinCliqueSize,
                String.format("Размер клики для %s должен быть не меньше %d. Получено: %d",
                        fileName, expectedMinCliqueSize, result.clique().size())
        );
    }

    private void validateClique(List<Edge> edges, List<Integer> clique, String fileName) {
        for (int i = 0; i < clique.size(); i++) {
            for (int j = i + 1; j < clique.size(); j++) {
                int vertex1 = clique.get(i);
                int vertex2 = clique.get(j);

                boolean edgeExists = edges.stream()
                        .anyMatch(edge ->
                                (edge.from() == vertex1 && edge.to() == vertex2) ||
                                        (edge.from() == vertex2 && edge.to() == vertex1));

                assertTrue(edgeExists,
                        String.format("В графе %s отсутствует ребро между вершинами %d и %d в найденной клике",
                                fileName, vertex1, vertex2));
            }
        }

        System.out.println("✓ Клика валидна для файла: " + fileName);
    }
}