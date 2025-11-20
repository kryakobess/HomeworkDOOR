package ru.homework.door.lab3;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CliqueSearchImprover {
    List<Integer> findBetterClique(Map<Integer, Set<Integer>> graph, List<Integer> clique);
}
