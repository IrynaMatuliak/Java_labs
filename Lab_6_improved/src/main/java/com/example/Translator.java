package com.example;

import java.util.HashMap;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class Translator {
    private HashMap<String, String> dictionary;
    private GoogleTranslateService googleTranslateService;
    private static final Pattern ENGLISH_WORD_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern UKRAINIAN_WORD_PATTERN = Pattern.compile("^[а-яА-ЯіІїЇєЄґҐ'\\-\\s]+$");

    public Translator() {
        this.dictionary = new HashMap<>();
        this.googleTranslateService = new GoogleTranslateService();
        addDefaultWords();
    }

    public void addWord(String englishWord, String ukrainianWord) {
        if (!isValidEnglishWord(englishWord)) {
            System.err.println("Помилка: некоректне англійське слово - '" + englishWord + "'");
            return;
        }
        if (!isValidUkrainianWord(ukrainianWord)) {
            System.err.println("Помилка: некоректне українське слово - '" + ukrainianWord + "'");
            return;
        }
        if (englishWord != null && ukrainianWord != null) {
            String key = englishWord.toLowerCase().trim();
            if (dictionary.containsKey(key)) {
                System.out.println("Увага: слово '" + englishWord + "' вже є в словнику. Перезаписую...");
            }
            dictionary.put(key, ukrainianWord.trim());
        }
    }

    public String translate(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            System.err.println("Помилка: отримана порожня фраза");
            return "";
        }
        String cleanPhrase = phrase.trim();
        if (!isValidEnglishPhrase(cleanPhrase)) {
            System.err.println("Попередження: фраза містить некоректні символи - '" + cleanPhrase + "'");
        }
        String fullPhraseTranslation = dictionary.get(cleanPhrase.toLowerCase());
        if (fullPhraseTranslation != null) {
            return fullPhraseTranslation;
        }
        String localTranslation = translateWithLocalDictionary(cleanPhrase);
        if (isCompleteTranslation(localTranslation)) {
            return localTranslation;
        }
        System.out.println("[Використовуємо Google Translate]");
        String googleTranslation = googleTranslateService.translatePhrase(cleanPhrase);

        if (googleTranslation != null && !googleTranslation.isEmpty() && !googleTranslation.equals(cleanPhrase)) {
            addWordsFromPhraseToDictionary(cleanPhrase, googleTranslation);
            return googleTranslation;
        }
        return localTranslation;
    }

    private String translateWithLocalDictionary(String phrase) {
        if (phrase == null || phrase.isEmpty()) {
            return "";
        }
        String[] words = phrase.split("\\s+");
        StringJoiner translatedPhrase = new StringJoiner(" ");
        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (cleanWord.isEmpty()) {
                translatedPhrase.add(word);
                continue;
            }
            String translation = dictionary.get(cleanWord);
            if (translation != null) {
                translatedPhrase.add(translation);
            } else {
                translatedPhrase.add(word);
            }
        }
        return translatedPhrase.toString();
    }

    private boolean isCompleteTranslation(String translation) {
        return translation != null && !translation.matches(".*[a-zA-Z].*");
    }

    private void addWordsFromPhraseToDictionary(String englishPhrase, String ukrainianPhrase) {
        if (englishPhrase == null || ukrainianPhrase == null) {
            return;
        }
        String[] englishWords = englishPhrase.split("\\s+");
        for (String englishWord : englishWords) {
            String cleanEnglishWord = englishWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (!cleanEnglishWord.isEmpty() && !dictionary.containsKey(cleanEnglishWord)) {
                String wordTranslation = googleTranslateService.translateWord(cleanEnglishWord);
                if (wordTranslation != null && !wordTranslation.isEmpty() &&
                        !wordTranslation.equals(cleanEnglishWord) &&
                        isValidUkrainianWord(wordTranslation)) {
                    dictionary.put(cleanEnglishWord, wordTranslation);
                    System.out.println("[Додано до словника: " + cleanEnglishWord + " -> " + wordTranslation + "]");
                }
            }
        }
        if (!dictionary.containsKey(englishPhrase.toLowerCase())) {
            dictionary.put(englishPhrase.toLowerCase(), ukrainianPhrase);
            System.out.println("[Додано фразу до словника: " + englishPhrase + " -> " + ukrainianPhrase + "]");
        }
    }

    public int getDictionarySize() {
        return dictionary.size();
    }

    public HashMap<String, String> getDictionary() {
        return new HashMap<>(dictionary);
    }

    public void addPhrase(String englishPhrase, String ukrainianPhrase) {
        if (!isValidEnglishPhrase(englishPhrase)) {
            System.err.println("Помилка: некоректна англійська фраза - '" + englishPhrase + "'");
            return;
        }
        if (!isValidUkrainianPhrase(ukrainianPhrase)) {
            System.err.println("Помилка: некоректна українська фраза - '" + ukrainianPhrase + "'");
            return;
        }
        if (englishPhrase != null && ukrainianPhrase != null) {
            dictionary.put(englishPhrase.toLowerCase().trim(), ukrainianPhrase.trim());
        }
    }

    private void addDefaultWords() {
        String[][] defaultWords = {
                {"hello", "привіт"},
                {"world", "світ"},
                {"java", "джава"},
                {"programming", "програмування"},
                {"computer", "комп'ютер"},
                {"book", "книга"},
                {"house", "будинок"},
                {"car", "автомобіль"},
                {"city", "місто"},
                {"people", "люди"},
                {"time", "час"},
                {"day", "день"},
                {"water", "вода"},
                {"sun", "сонце"},
                {"moon", "місяць"},
                {"good", "добре"},
                {"morning", "ранок"},
                {"thank", "дякую"},
                {"you", "ти/ви"},
                {"fill", "заповнити"},
                {"dictionary", "словник"},
                {"default", "за замовчуванням"},
                {"words", "слова"},
                {"phrases", "фрази"}
        };
        for (String[] pair : defaultWords) {
            if (isValidEnglishWord(pair[0]) && isValidUkrainianWord(pair[1])) {
                dictionary.put(pair[0].toLowerCase(), pair[1]);
            }
        }
        String[][] defaultPhrases = {
                {"good morning", "доброго ранку"},
                {"thank you", "дякую"},
                {"how are you", "як справи"},
                {"i love you", "я тебе люблю"},
                {"fill dictionary", "заповнити словник"},
                {"default words", "слова за замовчуванням"},
                {"default phrases", "фрази за замовчуванням"}
        };
        for (String[] pair : defaultPhrases) {
            if (isValidEnglishPhrase(pair[0]) && isValidUkrainianPhrase(pair[1])) {
                dictionary.put(pair[0].toLowerCase(), pair[1]);
            }
        }
    }

    private boolean isValidEnglishWord(String word) {
        return word != null && !word.trim().isEmpty() && ENGLISH_WORD_PATTERN.matcher(word.trim()).matches();
    }

    private boolean isValidUkrainianWord(String word) {
        return word != null && !word.trim().isEmpty() && UKRAINIAN_WORD_PATTERN.matcher(word.trim()).matches();
    }

    private boolean isValidEnglishPhrase(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            return false;
        }
        return phrase.matches("^[a-zA-Z\\s']+$");
    }

    private boolean isValidUkrainianPhrase(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            return false;
        }
        return phrase.matches("^[а-яА-ЯіІїЇєЄґҐ'\\-\\s]+$");
    }
}