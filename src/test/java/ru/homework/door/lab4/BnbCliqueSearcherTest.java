package ru.homework.door.lab4;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.homework.door.common.GraphData;
import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab2.MaximumCliqueSearcher;
import ru.homework.door.lab2.dto.MaximumCliqueResult;
import ru.homework.door.lab2.impl.GreedyRandomizedCliqueSearcher;
import ru.homework.door.lab4.impl.ColoringBnbCliqueSearcher;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.homework.door.common.GraphInputParser.parseGraphFile;

class BnbCliqueSearcherTest {

    private MaximumCliqueSearcher cliqueSearcher = new ColoringBnbCliqueSearcher(new GreedyRandomizedCliqueSearcher());

    private static Stream<Arguments> provideFilesToTest() {
        return Stream.of(
                Arguments.of("brock200_1.clq", 21),
                Arguments.of("brock200_2.clq", 12),
                Arguments.of("brock200_3.clq", 15),
                Arguments.of("brock200_4.clq", 17),
                Arguments.of("C125.9.clq", 34),
                Arguments.of("gen200_p0.9_44.clq", 44),
                Arguments.of("gen200_p0.9_55.clq", 55),
                Arguments.of("hamming8-4.clq", 16),
                Arguments.of("johnson16-2-4.clq", 8),
                Arguments.of("johnson8-2-4.clq", 4),
                Arguments.of("keller4.clq", 11),
                Arguments.of("MANN_a27.clq", 126),
                Arguments.of("MANN_a9.clq", 16),
                Arguments.of("p_hat1000-1.clq", 10),
                Arguments.of("p_hat1500-1.clq", 12),
                Arguments.of("p_hat300-3.clq", 36),
                Arguments.of("san1000.clq", 15),
                Arguments.of("sanr200_0.9.clq", 42)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilesToTest")
    void testDataFromFile(String fileName, int expectedMinCliqueSize) {
        GraphData graphData = parseGraphFile(Paths.get("src", "main", "resources", "maximum_clique", fileName).toFile());

        MaximumCliqueResult result = cliqueSearcher.findMaxClique(
                graphData.verticesCount,
                graphData.edges,
                0.5,  // alpha параметр
                1000  // maxIterations параметр
        );

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