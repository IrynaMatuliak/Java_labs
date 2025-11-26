package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringReflector reflector = new StringReflector();

        String literalString = "Hello world";
        System.out.println("\n1. String literal to change: " + literalString);

        System.out.print("Enter a new value for the string literal: ");
        String newLiteralValue = scanner.nextLine();
        reflector.modifyString(literalString, newLiteralValue);
        System.out.println("String literal after change: " + literalString);

        System.out.print("\nEnter a string: ");
        String inputString = scanner.nextLine();
        System.out.println("2. Input string to change: " + inputString);

        System.out.print("Enter a new value for the string: ");
        String newInputValue = scanner.nextLine();
        reflector.modifyString(inputString, newInputValue);
        System.out.println("The string after change: " + inputString);

        String programString = "Initial string";
        System.out.println("\n3. A string with a programmatically specified value before changing: " + programString);

        String programNewValue = "New string 1";
        System.out.println("Program value to replace: " + programNewValue);

        reflector.modifyString(programString, programNewValue);
        System.out.println("The string after change: " + programString);

        System.out.print("\nEnter another string: ");
        String anotherInputString = scanner.nextLine();
        System.out.println("4. Second string before change: " + anotherInputString);

        String anotherProgramValue = "New string 2";
        System.out.println("Program value to replace: " + anotherProgramValue);

        reflector.modifyString(anotherInputString, anotherProgramValue);
        System.out.println("The string after change: " + anotherInputString);

        scanner.close();
    }
}