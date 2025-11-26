package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class ArraySumTest {

    @Test
    public void testComputeSmallArray() {
        int[] array = {1, 2, 3, 4, 5};
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(15, result);
    }

    @Test
    public void testComputeThresholdSizeArray() {
        int[] array = new int[20];
        Arrays.fill(array, 2);
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(40, result);
    }

    @Test
    public void testComputeLargeArray() {
        int[] array = new int[100];
        Arrays.fill(array, 3);
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(300, result);
    }

    @Test
    public void testComputeEmptyArray() {
        int[] array = {};
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(0, result);
    }

    @Test
    public void testComputeArrayWithSingleElement() {
        int[] array = {10};
        ArraySum task = new ArraySum(array, 0, array.length);
        long result = task.compute();

        assertEquals(10, result);
    }

    @Test
    public void testComputeNegativeNumbers() {
        int[] array = {-1, -2, -3, -4, -5};
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(-15, result);
    }

    @Test
    public void testComputeMixedPositiveAndNegative() {
        int[] array = {1, -1, 2, -2, 3, -3};
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(0, result);
    }

    @Test
    public void testComputeVeryLargeArray() {
        int[] array = new int[1_000];
        Arrays.fill(array, 5);
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(5_000, result);
    }

    @Test
    public void testComputeZeroValues() {
        int[] array = {0, 0, 0, 0, 0, 0, 0, 0};
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(0, result);
    }

    @Test
    public void testComputeLargeNumbers() {
        int[] array = {Integer.MAX_VALUE, 1, Integer.MIN_VALUE, 1};
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        long expected = (long) Integer.MAX_VALUE + 1 + Integer.MIN_VALUE + 1;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeConstantArray() {
        int[] array = new int[25];
        Arrays.fill(array, 7);
        ArraySum task = new ArraySum(array, 0, array.length);

        long result = task.compute();

        assertEquals(175, result);
    }
}