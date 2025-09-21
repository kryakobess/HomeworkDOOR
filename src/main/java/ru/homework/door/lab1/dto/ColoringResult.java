package ru.homework.door.lab1.dto;

import java.util.List;

public record ColoringResult(
        List<List<Integer>> verticesGroupedByColor
) {
}
