package ru.homework.door.common;

import ru.homework.door.common.structures.Edge;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GraphInputParser {

    public static GraphData parseGraphFile(File file) {
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
}
