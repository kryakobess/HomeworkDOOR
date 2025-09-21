package ru.homework.door.common.structures;

import java.util.*;

public class DecreaseKeyBinaryHeap<T> {

    private final List<PriorityElement> array = new ArrayList<>();
    private final Map<T, Integer> positionMap = new HashMap<>();

    public T getMinAndRemove() {
        T rootValue = array.getFirst().value;
        positionMap.remove(rootValue);

        var lastElement = array.getLast();
        array.set(0, lastElement);
        positionMap.put(lastElement.value, 0);

        array.removeLast();
        shiftDown(0);

        return rootValue;
    }

    public void add(T value, int priority) {
        array.add(new PriorityElement(value, priority));
        positionMap.put(value, array.size() - 1);
        shiftUp(array.size() - 1);
    }

    public void decreaseKey(T value) {
        Integer index = positionMap.get(value);
        array.get(index).priority--;
        shiftUp(index);
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    private PriorityElement getParent(int i) {
        return array.get(getParentIndex(i));
    }

    private PriorityElement getRight(int i) {
        return array.get(getRightIndex(i));
    }

    private PriorityElement getLeft(int i) {
        return array.get(getLeftIndex(i));
    }

    private int getLeftIndex(int i) {
        return 2 * i + 1;
    }

    private int getRightIndex(int i) {
        return 2 * i + 2;
    }

    private int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    private void shiftDown(int i) {
        int minIndex = i;
        if (getLeftIndex(i) < array.size() && getLeft(i).priority < array.get(i).priority) {
            minIndex = getLeftIndex(i);
        }
        if (getRightIndex(i) < array.size() && getRight(i).priority < array.get(minIndex).priority) {
            minIndex = getRightIndex(i);
        }
        if (minIndex != i) {
            swapElements(i, minIndex);
            shiftDown(minIndex);
        }
    }

    private void shiftUp(int i) {
        while (i > 0 && array.get(i).priority < getParent(i).priority) {
            swapElements(i, getParentIndex(i));
            i = getParentIndex(i);
        }
    }

    private void swapElements(int i, int j) {
        PriorityElement temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);

        positionMap.put(array.get(i).value, i);
        positionMap.put(array.get(j).value, j);
    }

    protected class PriorityElement{
        int priority;
        T value;

        protected PriorityElement(T value, int priority) {
            this.value = value;
            this.priority = priority;
        }
    }
}
