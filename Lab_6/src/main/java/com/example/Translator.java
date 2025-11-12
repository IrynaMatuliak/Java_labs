package com.example;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Translator {
    private HashMap<String, String> dictionary;

    public Translator() {
        dictionary = new HashMap<>();
        initializeBasicDictionary();
    }

    private void initializeBasicDictionary() {
        dictionary.put("hello", "привіт");
        dictionary.put("world", "світ");
        dictionary.put("computer", "комп'ютер");
        dictionary.put("program", "програма");
        dictionary.put("good", "добрий");
        dictionary.put("morning", "ранок");
        dictionary.put("thank", "дякую");
        dictionary.put("you", "ви");
        dictionary.put("goodbye", "до побачення");
        dictionary.put("book", "книга");
        dictionary.put("house", "будинок");
        dictionary.put("cat", "кіт");
        dictionary.put("dog", "собака");
        dictionary.put("sun", "сонце");
        dictionary.put("moon", "місяць");
        dictionary.put("thank you", "дякую");
        dictionary.put("good morning", "добрий ранок");
        dictionary.put("good night", "добраніч");
        dictionary.put("the", "");
        dictionary.put("a", "");
        dictionary.put("an", "");
    }

    public void addWordPair(String englishWord, String ukrainianTranslation) {
        if (englishWord == null || ukrainianTranslation == null) {
            return;
        }
        String eng = englishWord.trim();
        String ukr = ukrainianTranslation.trim();
        if (!eng.isEmpty() && !ukr.isEmpty()) {
            dictionary.put(eng.toLowerCase(), ukr);
        }
    }

    public String translatePhrase(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            return "[Порожня фраза]";
        }
        String lowerPhrase = phrase.toLowerCase().trim();
        if (dictionary.containsKey(lowerPhrase)) {
            return dictionary.get(lowerPhrase);
        }
        String[] words = extractAndCleanWords(phrase);
        StringBuilder translatedPhrase = new StringBuilder();
        for (String word : words) {
            String translation = dictionary.get(word);
            if (translation != null) {
                translatedPhrase.append(translation).append(" ");
            } else {
                translatedPhrase.append("[").append(word).append("?] ");
            }
        }
        return translatedPhrase.toString().trim();
    }

    private String[] extractAndCleanWords(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            return new String[0];
        }
        String lowerPhrase = phrase.toLowerCase().trim();
        String[] words = lowerPhrase.split("\\s+");
        List<String> cleanedWords = new ArrayList<>();
        for (String word : words) {
            String cleanedWord = word.replaceAll("[^a-zA-Z']", "");
            if (!cleanedWord.isEmpty()) {
                cleanedWords.add(cleanedWord);
            }
        }
        return cleanedWords.toArray(new String[0]);
    }

    public boolean isPhraseInDictionary(String phrase) {
        return dictionary.containsKey(phrase.toLowerCase().trim());
    }

    public String[] getUnknownWords(String phrase) {
        String[] words = extractAndCleanWords(phrase);
        List<String> unknownWords = new ArrayList<>();
        for (String word : words) {
            if (!dictionary.containsKey(word)) {
                unknownWords.add(word);
            }
        }
        return unknownWords.toArray(new String[0]);
    }

    public int getDictionarySize() {
        return dictionary.size();
    }

    public HashMap<String, String> getDictionary() {
        return new HashMap<>(dictionary);
    }
}