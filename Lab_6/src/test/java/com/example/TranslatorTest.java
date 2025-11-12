package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    private Translator translator;

    @BeforeEach
    public void setUp() {
        translator = new Translator();
    }

    @Test
    public void testTranslateExistingWord() {
        String result = translator.translatePhrase("hello");
        assertEquals("привіт", result);
    }

    @Test
    public void testTranslateExistingPhrase() {
        String result = translator.translatePhrase("good morning");
        assertEquals("добрий ранок", result);
    }

    @Test
    public void testTranslateNonExistingWord() {
        String result = translator.translatePhrase("unknownword");
        assertEquals("[unknownword?]", result);
    }

    @Test
    public void testTranslateMixedPhrase() {
        String result = translator.translatePhrase("hello unknownworld");
        assertEquals("привіт [unknownworld?]", result);
    }

    @Test
    public void testTranslateEmptyPhrase() {
        String result = translator.translatePhrase("");
        assertEquals("[Порожня фраза]", result);
    }

    @Test
    public void testTranslateNullPhrase() {
        String result = translator.translatePhrase(null);
        assertEquals("[Порожня фраза]", result);
    }

    @Test
    public void testTranslateWithPunctuation() {
        String result = translator.translatePhrase("hello, world!");
        assertEquals("привіт світ", result);
    }

    @Test
    public void testAddWordPair() {
        translator.addWordPair("test", "тест");
        String result = translator.translatePhrase("test");
        assertEquals("тест", result);
    }

    @Test
    public void testAddWordPairWithEmptyValues() {
        int initialSize = translator.getDictionarySize();
        translator.addWordPair("", "тест");
        translator.addWordPair("test", "");
        translator.addWordPair("", "");
        assertEquals(initialSize, translator.getDictionarySize());
    }

    @Test
    public void testAddWordPairWithNullValues() {
        int initialSize = translator.getDictionarySize();
        translator.addWordPair(null, "тест");
        translator.addWordPair("test", null);
        translator.addWordPair(null, null);
        assertEquals(initialSize, translator.getDictionarySize());
    }

    @Test
    public void testIsPhraseInDictionary() {
        assertTrue(translator.isPhraseInDictionary("hello"));
        assertTrue(translator.isPhraseInDictionary("good morning"));
        assertFalse(translator.isPhraseInDictionary("nonexistent"));
    }

    @Test
    public void testGetUnknownWords() {
        String[] unknownWords = translator.getUnknownWords("hello nonexistent test");
        assertEquals(2, unknownWords.length);
        assertEquals("nonexistent", unknownWords[0]);
        assertEquals("test", unknownWords[1]);
    }

    @Test
    public void testGetUnknownWordsEmptyPhrase() {
        String[] unknownWords = translator.getUnknownWords("");
        assertEquals(0, unknownWords.length);
    }

    @Test
    public void testGetUnknownWordsNullPhrase() {
        String[] unknownWords = translator.getUnknownWords(null);
        assertEquals(0, unknownWords.length);
    }

    @Test
    public void testGetDictionarySize() {
        int initialSize = translator.getDictionarySize();
        translator.addWordPair("newword", "новеслово");
        assertEquals(initialSize + 1, translator.getDictionarySize());
    }

    @Test
    public void testGetDictionary() {
        var dictionary = translator.getDictionary();
        assertNotNull(dictionary);
        assertTrue(dictionary.containsKey("hello"));
        assertEquals("привіт", dictionary.get("hello"));
    }

    @Test
    public void testCaseInsensitiveTranslation() {
        String result1 = translator.translatePhrase("HELLO");
        String result2 = translator.translatePhrase("Hello");
        String result3 = translator.translatePhrase("hElLo");
        assertEquals("привіт", result1);
        assertEquals("привіт", result2);
        assertEquals("привіт", result3);
    }

    @Test
    public void testMultipleSpaces() {
        String result = translator.translatePhrase("hello    world");
        assertEquals("привіт світ", result);
    }
}