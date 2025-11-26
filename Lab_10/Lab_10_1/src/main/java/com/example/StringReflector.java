package com.example;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class StringReflector {
    public void modifyString(String targetString, String newValue) {
        try {
            Class<?> stringClass = String.class;
            Field valueField = stringClass.getDeclaredField("value");
            valueField.setAccessible(true);

            byte[] oldValue = (byte[]) valueField.get(targetString);
            byte[] newBytes = newValue.getBytes(StandardCharsets.UTF_8);

            if (newBytes.length > oldValue.length) {
                valueField.set(targetString, newBytes);
            } else {
                System.arraycopy(newBytes, 0, oldValue, 0, newBytes.length);
                for (int i = newBytes.length; i < oldValue.length; i++) {
                    oldValue[i] = 0;
                }
            }

            System.out.println("The string was successfully changed");
        } catch (Exception e) {
            System.err.println("Error while changing string: " + e.getMessage());
            e.printStackTrace();
        }
    }
}