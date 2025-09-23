package ru.homework.door.lab1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.homework.door.common.structures.Edge;
import ru.homework.door.lab1.dto.ColoringResult;
import ru.homework.door.lab1.impl.SmallestDegreeLastGraphColorist;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.homework.door.lab1.ColoristValidator.validateColoring;

class SDLGraphColoristFileTest {

    private final GraphColorist colorist = new SmallestDegreeLastGraphColorist();

    @ParameterizedTest
    @MethodSource("provideFilesToTest")
    void testDataFromFile(String fileName, int expectedColors) {
        GraphData graphData = parseGraphFile(Paths.get("src", "main", "resources", "vertex_coloring", fileName).toFile());

        ColoringResult result = colorist.colorGraph(graphData.verticesCount, graphData.edges);

        assertNotNull(result, "Результат не должен быть null");
        assertNotNull(result.verticesGroupedByColor(), "Список цветов не должен быть null");
        assertFalse(result.verticesGroupedByColor().isEmpty(), "Должен быть хотя бы один цвет");

        validateColoring(graphData.verticesCount, graphData.edges, result, fileName);
        System.out.println("Vertices grouped by color: " + result.verticesGroupedByColor());

        System.out.printf("Хроматическое число для %s должно быть меньше или равно %d. Полученное число: %d.%n", fileName, expectedColors, result.verticesGroupedByColor().size());
        assertTrue(
                expectedColors >= result.verticesGroupedByColor().size(),
                String.format("Хроматическое число для %s должно быть меньше или равно %d.", fileName, expectedColors)
        );
    }

    private static Stream<Arguments> provideFilesToTest() {
        return Stream.of(
                Arguments.of("myciel3.col", 4),
                Arguments.of("myciel7.col", 8),
                Arguments.of("school1.col", 15),
                Arguments.of("school1_nsh.col", 21),
                Arguments.of("anna.col", 11),
                Arguments.of("miles1000.col", 42),
                Arguments.of("miles1500.col", 73),
                Arguments.of("le450_5a.col", 11),
                Arguments.of("le450_15b.col", 18),
                Arguments.of("queen11_11.col", 16)
        );
    }

    private GraphData parseGraphFile(File file) {
        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            int verticesCount = 0;
            int edgesCount = 0;
            List<Edge> edges = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("c") || line.isEmpty()) {
                    continue; // Пропускаем комментарии
                }

                if (line.startsWith("p edge")) {
                    String[] parts = line.split("\\s+");
                    verticesCount = Integer.parseInt(parts[2]);
                    edgesCount = Integer.parseInt(parts[3]);
                    continue;
                }

                if (line.startsWith("e")) {
                    String[] parts = line.split("\\s+");
                    int from = Integer.parseInt(parts[1]);
                    int to = Integer.parseInt(parts[2]);
                    edges.add(new Edge(from, to));
                }
            }

            if (verticesCount == 0) {
                throw new IllegalArgumentException("Не удалось определить количество вершин");
            }

            return new GraphData(verticesCount, edgesCount, edges);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла: " + file.getAbsolutePath(), e);
        }
    }

    private static class GraphData {
        final int verticesCount;
        final int edgesCount;
        final List<Edge> edges;

        GraphData(int verticesCount, int edgesCount, List<Edge> edges) {
            this.verticesCount = verticesCount;
            this.edgesCount = edgesCount;
            this.edges = edges;
        }
    }
}
