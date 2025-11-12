package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DictionaryManager manager = new DictionaryManager();

        System.out.println("--- Англо-український перекладач ---");
        System.out.println("Словник вже містить базові слова та фрази");
        boolean exitProgram = false;
        while (!exitProgram) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ ---");
            System.out.println("1 - Додати слова до словника");
            System.out.println("2 - Перейти до перекладу");
            System.out.println("3 - Показати розмір словника");
            System.out.println("4 - Показати всі слова словника");
            System.out.println("5 - Вийти з програми");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    manager.fillDictionary();
                    break;
                case "2":
                    manager.translatePhrases();
                    break;
                case "3":
                    System.out.println("\nЗагальний розмір словника: " +
                            manager.getTranslator().getDictionarySize() + " слів/фраз");
                    break;
                case "4":
                    manager.showAllDictionary();
                    break;
                case "5":
                    exitProgram = true;
                    System.out.println("Дякуємо за використання перекладача! До побачення!");
                    break;
                default:
                    System.out.println("Помилка: Будь ласка, введіть число від 1 до 5");
                    break;
            }
        }
        scanner.close();
    }
}