package com.example;

import java.util.Scanner;
import java.util.HashMap;

public class DictionaryManager {
    private Translator translator;
    private Scanner scanner;

    public DictionaryManager() {
        translator = new Translator();
        scanner = new Scanner(System.in);
    }

    public void fillDictionary() {
        System.out.println("\n--- Додавання слів до словника ---");
        System.out.println("Введіть пари слів у форматі: англійське_слово:український_переклад");
        System.out.println("АБО: англійське_слово - український_переклад");
        System.out.println("Для фраз використовуйте лапки: \"англійська фраза\" - \"український переклад\"");
        System.out.println("Для повернення до головного меню введіть 'back'");
        while (true) {
            System.out.print("\nВведіть пару слів: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                System.out.println("Повернення до головного меню...");
                break;
            }
            if (input.equalsIgnoreCase("stop")) {
                System.out.println("Завершено додавання слів. Загальний розмір словника: " + translator.getDictionarySize() + " слів/фраз");
                break;
            }
            if (input.matches("\".*\"\\s*[-:]\\s*\".*\"")) {
                processQuotedInput(input);
            } else {
                processRegularInput(input);
            }
        }
    }

    private void processQuotedInput(String input) {
        String[] parts = input.split("\\s*[-:]\\s*", 2);
        if (parts.length == 2) {
            String englishPhrase = parts[0].replaceAll("^\"|\"$", "").trim();
            String ukrainianPhrase = parts[1].replaceAll("^\"|\"$", "").trim();
            if (!englishPhrase.isEmpty() && !ukrainianPhrase.isEmpty()) {
                translator.addWordPair(englishPhrase, ukrainianPhrase);
                System.out.println("Додано фразу: '" + englishPhrase + "' -> '" + ukrainianPhrase + "'");
            } else {
                System.out.println("Помилка: Обидва рядки в лапках повинні бути не порожніми");
            }
        } else {
            System.out.println("Помилка: Неправильний формат лапок. Використовуйте: \"фраза\" - \"переклад\"");
        }
    }

    private void processRegularInput(String input) {
        String[] words = input.split(":", 2);
        if (words.length != 2) {
            words = input.split("\\s+-\\s+", 2);
        }
        if (words.length == 2) {
            String englishWord = words[0].trim();
            String ukrainianWord = words[1].trim();
            if (!englishWord.isEmpty() && !ukrainianWord.isEmpty()) {
                if (englishWord.contains(" ")) {
                    System.out.println("Додано фразу: '" + englishWord + "' -> '" + ukrainianWord + "'");
                } else {
                    System.out.println("Додано: '" + englishWord + "' -> '" + ukrainianWord + "'");
                }
                translator.addWordPair(englishWord, ukrainianWord);
            } else {
                System.out.println("Помилка: Обидва слова повинні бути не порожніми");
            }
        } else {
            System.out.println("Помилка: Неправильний формат. Використовуйте:");
            System.out.println("  слово:переклад");
            System.out.println("  слово - переклад");
            System.out.println("  \"фраза\" - \"переклад\"");
            System.out.println("  back - для повернення до головного меню");
        }
    }

    public void translatePhrases() {
        System.out.println("\n--- Переклад фраз/слів ---");
        System.out.println("Введіть фразу/слово англійською мовою для перекладу");
        System.out.println("Для повернення до головного меню введіть 'back'");
        while (true) {
            System.out.print("\nВведіть фразу: ");
            String phrase = scanner.nextLine().trim();
            if (phrase.equalsIgnoreCase("back")) {
                System.out.println("Повернення до головного меню...");
                break;
            }
            if (phrase.equalsIgnoreCase("exit")) {
                System.out.println("Повернення до головного меню...");
                break;
            }
            if (!phrase.isEmpty()) {
                String translation = translator.translatePhrase(phrase);
                System.out.println("Переклад: " + translation);
                if (!translator.isPhraseInDictionary(phrase)) {
                    offerToAddUnknownWords(phrase);
                }
            } else {
                System.out.println("Помилка: Фраза не може бути порожньою");
            }
        }
    }

    private void offerToAddUnknownWords(String phrase) {
        String[] individualWords = translator.getUnknownWords(phrase);
        if (individualWords.length > 0) {
            System.out.println("\nУ словнику відсутні такі окремі слова:");
            for (String word : individualWords) {
                System.out.println("- " + word);
            }
            System.out.print("\nБажаєте додати відсутні слова до словника? (так/ні/back): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("back")) {
                System.out.println("Повернення до головного меню...");
                return;
            }
            if (response.equals("так") || response.equals("yes") || response.equals("т") || response.equals("y")) {
                addUnknownWordsInteractive(individualWords);
            }
        }
    }

    private void addUnknownWordsInteractive(String[] unknownWords) {
        for (String word : unknownWords) {
            if (!word.isEmpty()) {
                System.out.print("Введіть переклад для слова '" + word + "' (або 'back' для виходу): ");
                String translation = scanner.nextLine().trim();
                if (translation.equalsIgnoreCase("back")) {
                    System.out.println("Повернення до головного меню...");
                    break;
                }
                if (!translation.isEmpty()) {
                    translator.addWordPair(word, translation);
                    System.out.println("Додано: '" + word + "' -> '" + translation + "'");
                } else {
                    System.out.println("Пропущено слово: '" + word + "'");
                }
            }
        }
    }

    public void showAllDictionary() {
        System.out.println("\n--- ВСІ СЛОВА СЛОВНИКА ---");
        System.out.println("Загальна кількість: " + translator.getDictionarySize() + " слів/фраз\n");
        HashMap<String, String> dictionary = translator.getDictionary();
        if (dictionary.isEmpty()) {
            System.out.println("Словник порожній.");
            return;
        }
        int counter = 1;
        for (HashMap.Entry<String, String> entry : dictionary.entrySet()) {
            String englishWord = entry.getKey();
            String ukrainianWord = entry.getValue();
            boolean isPhrase = englishWord.contains(" ");
            String type = isPhrase ? "ФРАЗА" : "СЛОВО";
            System.out.printf("%3d. [%s] %-30s -> %s%n",
                    counter++, type, englishWord, ukrainianWord);
        }
        System.out.println("\n--- КІНЕЦЬ СЛОВНИКА ---");
        System.out.print("\nНатисніть Enter для продовження...");
        scanner.nextLine();
    }

    public Translator getTranslator() {
        return translator;
    }
}