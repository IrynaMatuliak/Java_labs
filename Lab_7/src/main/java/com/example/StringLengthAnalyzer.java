package com.example;

import java.util.Arrays;
import java.util.function.BiPredicate;

public class StringLengthAnalyzer {

    public static String[] findStringsLessThanAverage(String[] strings) {
        return filterStringsByAverage(strings, (str, avg) -> str.length() < avg);
    }

    public static String[] findStringsGreaterThanAverage(String[] strings) {
        return filterStringsByAverage(strings, (str, avg) -> str.length() > avg);
    }

    private static String[] filterStringsByAverage(String[] strings, BiPredicate<String, Double> condition) {
        double averageLength = calculateAverageLength(strings);
        return Arrays.stream(strings == null ? new String[0] : strings)
                .filter(str -> condition.test(str, averageLength))
                .toArray(String[]::new);
    }

    public static double calculateAverageLength(String[] strings) {
        return Arrays.stream(strings == null ? new String[0] : strings)
                .mapToInt(String::length)
                .average()
                .orElse(0.0);
    }
}