package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DictionaryManager manager = new DictionaryManager();
        System.out.println("--- Англо-український перекладач ---");
        System.out.println("Словник вже містить базові слова та фрази");
        String choice = "";
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("\nБажаєте додати додаткові слова до словника?");
            System.out.println("1 - Так, вручну");
            System.out.println("2 - Ні, перейти до перекладу");
            System.out.print("Ваш вибір: ");
            choice = scanner.nextLine().trim();
            if (choice.equals("1") || choice.equals("2")) {
                validChoice = true;
            } else {
                System.out.println("Помилка: Будь ласка, введіть '1' або '2'");
            }
        }
        if (choice.equals("1")) {
            manager.fillDictionary();
        }
        System.out.println("\nЗагальний розмір словника: " + manager.getTranslator().getDictionarySize() + " слів/фраз");
        manager.translatePhrases();
        System.out.println("\nДякуємо за використання перекладача!");
        scanner.close();
    }
}