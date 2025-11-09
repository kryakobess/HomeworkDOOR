package ru.homework.door.lab1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.homework.door.common.GraphData;
import ru.homework.door.lab1.dto.ColoringResult;
import ru.homework.door.lab1.impl.SmallestDegreeLastGraphColorist;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.homework.door.lab1.ColoristValidator.validateColoring;
import static ru.homework.door.common.GraphInputParser.parseGraphFile;

class SDLGraphColoristFileTest {

    private final GraphColorist colorist = new SmallestDegreeLastGraphColorist();

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

    @ParameterizedTest
    @MethodSource("provideFilesToTest")
    void testDataFromFile(String fileName, int expectedColors) {
        GraphData graphData = parseGraphFile(Paths.get("src", "main", "resources", "vertex_coloring", fileName).toFile());

        long startTime = System.nanoTime();

        ColoringResult result;
        result = colorist.colorGraph(graphData.verticesCount, graphData.edges);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf("Время выполнения = %.5f сек\n", duration / 1_000_000_000.0);

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

}
