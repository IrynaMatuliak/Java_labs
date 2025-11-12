package com.example;

import java.util.Scanner;
import java.util.regex.Pattern;

public class DictionaryManager {
    private Translator translator;
    private Scanner scanner;
    private static final Pattern INPUT_PATTERN = Pattern.compile("^[a-zA-Zа-яА-ЯіІїЇєЄґҐ'\\-\\s\"]+$");

    public DictionaryManager() {
        this.translator = new Translator();
        this.scanner = new Scanner(System.in);
    }

    public void fillDictionary() {
        System.out.println("--- Наповнення словника ---");
        System.out.println("Введіть пари слів у форматі: англійське_слово українське_слово");
        System.out.println("Або цілі фрази у форматі: \"англійська фраза\" \"українська фраза\"");
        System.out.println("Для завершення введення введіть 'stop'");
        int addedCount = 0;
        int errorCount = 0;
        while (true) {
            System.out.print("Введіть пару слів/фраз: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("stop")) {
                break;
            }
            if (!isValidInput(input)) {
                System.err.println("Помилка: введені дані містять некоректні символи");
                errorCount++;
                continue;
            }
            if (input.contains("\"")) {
                String[] phrases = input.split("\"\\s+\"");
                if (phrases.length == 2) {
                    String englishPhrase = phrases[0].replace("\"", "").trim();
                    String ukrainianPhrase = phrases[1].replace("\"", "").trim();

                    if (isValidPhrasePair(englishPhrase, ukrainianPhrase)) {
                        translator.addPhrase(englishPhrase, ukrainianPhrase);
                        System.out.println("Фраза додана до словника: \"" + englishPhrase + "\" -> \"" + ukrainianPhrase + "\"");
                        addedCount++;
                    } else {
                        errorCount++;
                    }
                    continue;
                }
            }
            String[] words = input.split("\\s+", 2);
            if (words.length == 2) {
                String englishWord = words[0].trim();
                String ukrainianWord = words[1].trim();
                if (isValidWordPair(englishWord, ukrainianWord)) {
                    translator.addWord(englishWord, ukrainianWord);
                    System.out.println("Слово додано до словника: " + englishWord + " -> " + ukrainianWord);
                    addedCount++;
                } else {
                    errorCount++;
                }
            } else {
                System.err.println("Неправильний формат. Очікується: англійське_слово українське_слово");
                errorCount++;
            }
        }
        System.out.println("\nПідсумок: додано " + addedCount + " слів/фраз, помилок: " + errorCount);
    }

    public void translatePhrases() {
        System.out.println("\n--- Переклад фраз ---");
        System.out.println("Введіть фрази англійською мовою для перекладу");
        System.out.println("Для виходу введіть 'exit'");
        System.out.println("Примітка:");
        System.out.println("- Знайомі фрази перекладаються з локального словника");
        System.out.println("- Нові фрази автоматично перекладаються через Google Translate");
        int translatedCount = 0;
        while (true) {
            System.out.print("\nВведіть фразу: ");
            String phrase = scanner.nextLine().trim();
            if (phrase.equalsIgnoreCase("exit")) {
                break;
            }
            if (phrase.isEmpty()) {
                System.err.println("Помилка: порожня фраза");
                continue;
            }
            if (!isValidEnglishInput(phrase)) {
                System.err.println("Попередження: фраза містить некоректні символи для англійської мови");
            }
            System.out.println("Оригінал: " + phrase);
            String translation = translator.translate(phrase);
            System.out.println("Переклад: " + translation);
            System.out.println("Поточний розмір словника: " + translator.getDictionarySize() + " слів/фраз");
            translatedCount++;
        }
        System.out.println("Перекладено фраз: " + translatedCount);
    }

    public Translator getTranslator() {
        return translator;
    }

    private boolean isValidInput(String input) {
        return input != null && INPUT_PATTERN.matcher(input).matches();
    }

    private boolean isValidEnglishInput(String input) {
        return input != null && input.matches("^[a-zA-Z\\s']+$");
    }

    private boolean isValidWordPair(String englishWord, String ukrainianWord) {
        if (englishWord == null || ukrainianWord == null) {
            System.err.println("Помилка: null значення");
            return false;
        }
        if (englishWord.isEmpty() || ukrainianWord.isEmpty()) {
            System.err.println("Помилка: порожні слова");
            return false;
        }
        if (!englishWord.matches("^[a-zA-Z]+$")) {
            System.err.println("Помилка: англійське слово містить некоректні символи - '" + englishWord + "'");
            return false;
        }
        if (!ukrainianWord.matches("^[а-яА-ЯіІїЇєЄґҐ'\\-]+$")) {
            System.err.println("Помилка: українське слово містить некоректні символи - '" + ukrainianWord + "'");
            return false;
        }
        return true;
    }

    private boolean isValidPhrasePair(String englishPhrase, String ukrainianPhrase) {
        if (englishPhrase == null || ukrainianPhrase == null) {
            System.err.println("Помилка: null значення");
            return false;
        }
        if (englishPhrase.isEmpty() || ukrainianPhrase.isEmpty()) {
            System.err.println("Помилка: порожні фрази");
            return false;
        }
        if (!englishPhrase.matches("^[a-zA-Z\\s']+$")) {
            System.err.println("Помилка: англійська фраза містить некоректні символи - '" + englishPhrase + "'");
            return false;
        }
        if (!ukrainianPhrase.matches("^[а-яА-ЯіІїЇєЄґҐ'\\-\\s]+$")) {
            System.err.println("Помилка: українська фраза містить некоректні символи - '" + ukrainianPhrase + "'");
            return false;
        }
        return true;
    }
}