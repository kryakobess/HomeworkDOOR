package ru.homework.door.lab2.impl;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.common.utils.GraphUtils;
import ru.homework.door.lab2.MaximumCliqueSearcher;
import ru.homework.door.lab2.dto.MaximumCliqueResult;
import ru.homework.door.lab3.CliqueSearchImprover;

import java.util.*;
import java.util.stream.Collectors;

public class GreedyRandomizedCliqueSearcher implements MaximumCliqueSearcher {

    @Override
    public MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations) {
        return findMaxClique(verticesCount, edges, alpha, maxIterations, null);
    }

    @Override
    public MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations, CliqueSearchImprover improver) {
        if (alpha < 0 || alpha > 1) throw new IllegalArgumentException("Alpha should be in range: [0;1]");
        Map<Integer, Set<Integer>> graph = GraphUtils.buildGraph(verticesCount, edges);
        Random random = new Random();

        List<Integer> maxClique = Collections.emptyList();
        for (int i = 0 ; i < maxIterations; ++i) {
            List<Integer> clique = findClique(graph, alpha, random);

            if (improver != null) {
                clique = improver.findBetterClique(graph, clique);
            }

            if (clique.size() > maxClique.size()) {
                maxClique = clique;
            }
        }

        return new MaximumCliqueResult(maxClique);
    }

    private List<Integer> findClique(Map<Integer, Set<Integer>> graph, double alpha, Random random) {
        List<Integer> clique = new ArrayList<>();
        Set<Integer> candidates = new HashSet<>(GraphUtils.getListOfVertices(graph.size()));

        while (!candidates.isEmpty()) {
            Map<Integer, Integer> scores = getVerticesScores(candidates, graph);
            List<Integer> restrictedCandidates = getRestrictedCandidates(scores, alpha);

            if (restrictedCandidates.isEmpty()) break;
            int randomCandidate = restrictedCandidates.get(random.nextInt(restrictedCandidates.size()));

            clique.add(randomCandidate);
            candidates.retainAll(graph.get(randomCandidate));
            candidates.remove(randomCandidate);
        }

        return clique;
    }

    private Map<Integer, Integer> getVerticesScores(Set<Integer> candidates, Map<Integer, Set<Integer>> graph) {
        Map<Integer, Integer> scores = new HashMap<>();
        for (var v : candidates) {
            var neighbors = graph.get(v);
            long score = candidates.stream()
                    .filter(neighbors::contains)
                    .count();
            scores.put(v, Math.toIntExact(score));
        }
        return scores;
    }

    private List<Integer> getRestrictedCandidates(Map<Integer, Integer> scores, double alpha) {
        Integer maxScore = Collections.max(scores.values());
        Integer minScore = Collections.min(scores.values());

        int threshold = (int) (maxScore - alpha * (maxScore - minScore));
        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
