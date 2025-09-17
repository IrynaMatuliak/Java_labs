package org.example;

import java.util.ArrayList;
import java.util.List;

public class StringLengthAnalyzer {

    public static String[] findStringsLessThanAverage(String[] strings) {
        if (strings == null || strings.length == 0) {
            return new String[0]; // Returns an empty array
        }
        double averageLength = calculateAverageLength(strings);
        return filterStrings(strings, averageLength, true);
    }

    public static String[] findStringsGreaterThanAverage(String[] strings) {
        if (strings == null || strings.length == 0) {
            return new String[0];
        }
        double averageLength = calculateAverageLength(strings);
        return filterStrings(strings, averageLength, false);
    }

    public static double calculateAverageLength(String[] strings) {
        int totalLength = 0;
        for (String str : strings) {
            totalLength += str.length();
        }
        return (double) totalLength / strings.length;
    }

    private static String[] filterStrings(String[] strings, double averageLength, boolean lessThan) {
        List<String> resultList = new ArrayList<>();
        for (String str : strings) {
            boolean conditionMet = lessThan ? (str.length() < averageLength) : (str.length() > averageLength);

            if (conditionMet) {
                resultList.add(str);
            }
        }
        return resultList.toArray(new String[0]);
    }
}