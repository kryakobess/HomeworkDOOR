package ru.homework.door.lab4.impl;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.common.utils.GraphUtils;
import ru.homework.door.lab1.impl.SmallestDegreeLastGraphColorist;
import ru.homework.door.lab2.MaximumCliqueSearcher;
import ru.homework.door.lab2.dto.MaximumCliqueResult;

import java.util.*;

public class ColoringBnbCliqueSearcher implements MaximumCliqueSearcher {

    private final MaximumCliqueSearcher initialHeuristic;
    private final SmallestDegreeLastGraphColorist smallestDegreeLastColorist = new SmallestDegreeLastGraphColorist();

    public ColoringBnbCliqueSearcher(MaximumCliqueSearcher initialHeuristic) {
        this.initialHeuristic = initialHeuristic;
    }

    @Override
    public MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations) {
        long startTime = System.nanoTime();

        List<Integer> clique = initialHeuristic.findMaxClique(verticesCount, edges, alpha, maxIterations).clique();

        long heuristicEndTime = System.nanoTime();
        System.out.printf("Время выполнения начальной эвристики = %.5f сек\n", (heuristicEndTime - startTime) / 1_000_000_000.0);

        Map<Integer, Set<Integer>> graph = GraphUtils.buildGraph(verticesCount, edges);
        List<Integer> pardalosOrder = new ArrayList<>(smallestDegreeLastColorist.makeOrdering(graph)).reversed();

        findBestClique(pardalosOrder, clique, new ArrayList<>(), graph);

        long bnbEndTime = System.nanoTime();
        System.out.printf("Время выполнения метода ветвей и границ = %.5f сек\n", (bnbEndTime - heuristicEndTime) / 1_000_000_000.0);
        return new MaximumCliqueResult(clique);
    }

    private void findBestClique(List<Integer> candidates, List<Integer> bestClique, List<Integer> currentClique, Map<Integer, Set<Integer>> graph) {
        if (candidates.isEmpty()) {
            if (currentClique.size() > bestClique.size()) {
                bestClique.clear();
                bestClique.addAll(currentClique);
            }
            return;
        }

        for (var iterator = candidates.iterator(); iterator.hasNext();) {
            Map<Integer, Integer> vertexColors = smallestDegreeLastColorist.colorVertices(candidates.reversed(), graph);
            var candidatesInColorsOrder = getCandidatesInColorsOrder(vertexColors);

            var v = candidatesInColorsOrder.getFirst().getKey();
            var color = candidatesInColorsOrder.getFirst().getValue();
            if (currentClique.size() + color <= bestClique.size()) {
                return;
            }

            List<Integer> clique = new ArrayList<>(currentClique);
            clique.add(v);
            candidates.remove(v);
            findBestClique(getCandidatesInOriginalOrder(v, candidates, graph), bestClique, clique, graph);
        }
    }

    private List<Integer> getCandidatesInOriginalOrder(Integer v, List<Integer> order, Map<Integer, Set<Integer>> graph) {
        List<Integer> candidates = new ArrayList<>();
        for (var candidate : order) {
            if (graph.get(v).contains(candidate)) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    private List<Map.Entry<Integer, Integer>> getCandidatesInColorsOrder(Map<Integer, Integer> vertexColors) {
        return vertexColors.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();
    }
}
