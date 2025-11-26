package com.example;

import java.util.concurrent.RecursiveTask;

public class ArraySum extends RecursiveTask<Long> {
    private static final int THRESHOLD = 20;
    private final int[] array;
    private final int start;
    private final int end;

    public ArraySum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }

        int middle = (start + end) / 2;
        ArraySum leftTask = new ArraySum(array, start, middle);
        ArraySum rightTask = new ArraySum(array, middle, end);

        leftTask.fork();
        rightTask.fork();

        long leftResult = leftTask.join();
        long rightResult = rightTask.join();

        return leftResult + rightResult;
    }
}