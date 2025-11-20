package ru.homework.door.lab3.impl;

import ru.homework.door.common.structures.Pair;
import ru.homework.door.lab3.CliqueSearchImprover;

import java.util.*;

public class TabuSearchImprover implements CliqueSearchImprover {

    private final static int NO_CANDIDATE = -1;

    @Override
    public List<Integer> findBetterClique(Map<Integer, Set<Integer>> graph, List<Integer> clique, int iterationsCount) {
        Set<Integer> cliqueSet = new HashSet<>(clique);
        Queue<Integer> tabuQueue = new ArrayDeque<>();
        Set<Integer> tabuSet = new HashSet<>();

        for (int i = 0; i < iterationsCount; ++i) {
            var candidates = getCandidates(graph, cliqueSet);

            for (var candidate : candidates) {
                int newSize = cliqueSet.size() + (candidate.left() == -1 ? 1 : 0);
                if
            }
        }
        return List.of();
    }

    private List<Pair<Integer, Integer>> getCandidates(Map<Integer, Set<Integer>> graph, Set<Integer> clique) {
        List<Pair<Integer, Integer>> moveSwapCandidates = new ArrayList<>();
        for (var v : graph.keySet()) {
            if (!clique.contains(v) && isConnectedToAllVertices(v, clique, graph)) {
                moveSwapCandidates.add(new Pair<>(-1, v)); // move
            }
        }

        for (var out : clique) {
            for (var in : graph.keySet()) {
                if (!clique.contains(in)) {
                    Set<Integer> tempClique = new HashSet<>(clique);
                    tempClique.remove(out);
                    if (isConnectedToAllVertices(in, tempClique, graph)) {
                        moveSwapCandidates.add(new Pair<>(out, in)); // swap
                    }
                }
            }
        }

        return moveSwapCandidates;
    }

    private void addToTabu(Integer v, Queue<Integer> tabuQueue, Set<Integer>) {

    }

    private boolean isConnectedToAllVertices(int v, Set<Integer> vertices, Map<Integer, Set<Integer>> graph) {
        var neighbors = graph.get(v);
        return neighbors.containsAll(vertices);
    }
}
