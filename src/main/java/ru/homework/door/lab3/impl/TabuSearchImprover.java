package ru.homework.door.lab3.impl;

import ru.homework.door.common.structures.Edge;
import ru.homework.door.common.structures.Pair;
import ru.homework.door.common.utils.GraphUtils;
import ru.homework.door.lab2.MaximumCliqueSearcher;
import ru.homework.door.lab2.dto.MaximumCliqueResult;
import ru.homework.door.lab3.dto.Tabu;

import java.util.*;

public class TabuSearchImprover implements MaximumCliqueSearcher {

    private final static int NO_CANDIDATE = -1;
    private final static int NO_IMPROVEMENT_LIMIT = 50;

    private final Random random = new Random(42);

    private final MaximumCliqueSearcher cliqueSearcher;
    private final int tabuCoolDown;
    private final int iterationsCount;

    public TabuSearchImprover(MaximumCliqueSearcher cliqueSearcher, int tabuCoolDown, int iterationsCount) {
        this.cliqueSearcher = cliqueSearcher;
        this.tabuCoolDown = tabuCoolDown;
        this.iterationsCount = iterationsCount;
    }

    @Override
    public MaximumCliqueResult findMaxClique(Integer verticesCount, List<Edge> edges, double alpha, int maxIterations) {
        var cliqueResult = cliqueSearcher.findMaxClique(verticesCount, edges, alpha, maxIterations);
        var clique = cliqueResult.clique();
        var graph = GraphUtils.buildGraph(verticesCount, edges);

        Set<Integer> currentClique = new HashSet<>(clique);
        Set<Integer> bestClique = new HashSet<>(clique);
        Tabu tabuAdd = new Tabu(tabuCoolDown);
        Tabu tabuRemove = new Tabu(tabuCoolDown);
        int noImprovementCount = 0;

        for (int i = 0; i < iterationsCount; ++i) {
            Pair<Integer, Integer> bestMove = findBestMove(graph, currentClique, bestClique.size(), tabuAdd, tabuRemove);
            if (bestMove != null) {
                removeFromClique(currentClique, bestMove.left(), tabuRemove);
                addToClique(currentClique, bestMove.right(), tabuAdd);
                if (currentClique.size() > bestClique.size()) {
                    bestClique = new HashSet<>(currentClique);
                    noImprovementCount = 0;
                } else {
                    noImprovementCount++;
                }
            } else {
                noImprovementCount++;
            }

            tabuAdd.expire();
            tabuRemove.expire();

            if (noImprovementCount > NO_IMPROVEMENT_LIMIT) {
                if (currentClique.size() > 1) {
                    dropRandomVertex(currentClique, tabuRemove);
                    noImprovementCount = 0;
                }
            }
        }

        return new MaximumCliqueResult(new ArrayList<>(bestClique));
    }

    private Pair<Integer, Integer> findBestMove(
            Map<Integer, Set<Integer>> graph,
            Set<Integer> clique,
            int bestCliqueSize,
            Tabu tabuAdd,
            Tabu tabuRemove
    ) {
        var candidates = getCandidates(graph, clique);
        int bestMoveSize = 0;
        Pair<Integer, Integer> bestMove = null;
        for (var moveSwap : candidates) {
            boolean tabu = tabuAdd.contains(moveSwap.right()) || (moveSwap.left() != NO_CANDIDATE && tabuRemove.contains(moveSwap.left()));
            int newSize = clique.size() + (moveSwap.left() == NO_CANDIDATE ? 1 : 0);

            if ((!tabu || newSize > bestCliqueSize) && (newSize > bestMoveSize)) {
                bestMoveSize = newSize;
                bestMove = moveSwap;
            }
        }
        return bestMove;
    }

    private List<Pair<Integer, Integer>> getCandidates(Map<Integer, Set<Integer>> graph, Set<Integer> clique) {
        List<Pair<Integer, Integer>> moveSwapCandidates = new ArrayList<>();
        for (var v : graph.keySet()) {
            if (!clique.contains(v) && isConnectedToAllVertices(v, clique, graph)) {
                moveSwapCandidates.add(new Pair<>(NO_CANDIDATE, v)); // move
            }
        }

        for (var out : clique) {
            for (var in : graph.keySet()) {
                if (!clique.contains(in)) {
                    Set<Integer> tempClique = new HashSet<>(clique);
                    tempClique.remove(out);
                    if (isConnectedToAllVertices(in, tempClique, graph)) {
                        moveSwapCandidates.add(new Pair<>(out, in)); // 1:1 swap
                    }
                }
            }
        }

        return moveSwapCandidates;
    }

    private void addToClique(Set<Integer> clique, Integer v, Tabu tabuAdd) {
        clique.add(v);
        tabuAdd.add(v);
    }

    private void removeFromClique(Set<Integer> clique, Integer v, Tabu tabuRemove) {
        if (NO_CANDIDATE != v) {
            clique.remove(v);
            tabuRemove.add(v);
        }
    }

    private void dropRandomVertex(Set<Integer> currentClique, Tabu tabuRemove) {
        List<Integer> cliqueList = new ArrayList<>(currentClique);
        int randomVertex = cliqueList.get(random.nextInt(cliqueList.size()));
        currentClique.remove(randomVertex);
        tabuRemove.add(randomVertex);
    }

    private boolean isConnectedToAllVertices(int v, Set<Integer> vertices, Map<Integer, Set<Integer>> graph) {
        var neighbors = graph.get(v);
        return neighbors.containsAll(vertices);
    }
}
