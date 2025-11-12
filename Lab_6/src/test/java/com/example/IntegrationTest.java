package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private DictionaryManager manager;

    @BeforeEach
    public void setUp() {
        manager = new DictionaryManager();
    }

    @Test
    public void testIntegrationAddAndTranslate() {
        manager.getTranslator().addWordPair("integration", "інтеграція");
        String result = manager.getTranslator().translatePhrase("integration");
        assertEquals("інтеграція", result);
    }

    @Test
    public void testIntegrationPhraseTranslation() {
        manager.getTranslator().addWordPair("how are you", "як справи");
        String result = manager.getTranslator().translatePhrase("how are you");
        assertEquals("як справи", result);
        assertTrue(manager.getTranslator().isPhraseInDictionary("how are you"));
    }

    @Test
    public void testIntegrationUnknownWords() {
        String phrase = "unknown phrase test";
        String[] unknownWords = manager.getTranslator().getUnknownWords(phrase);
        assertEquals(3, unknownWords.length);
        assertEquals("unknown", unknownWords[0]);
        assertEquals("phrase", unknownWords[1]);
        assertEquals("test", unknownWords[2]);
    }

    @Test
    public void testIntegrationDictionarySize() {
        int initialSize = manager.getTranslator().getDictionarySize();
        manager.getTranslator().addWordPair("test1", "тест1");
        manager.getTranslator().addWordPair("test2", "тест2");
        assertEquals(initialSize + 2, manager.getTranslator().getDictionarySize());
    }

    @Test
    public void testIntegrationMultipleOperations() {
        manager.getTranslator().addWordPair("apple", "яблуко");
        manager.getTranslator().addWordPair("red apple", "червоне яблуко");
        assertTrue(manager.getTranslator().isPhraseInDictionary("apple"));
        assertTrue(manager.getTranslator().isPhraseInDictionary("red apple"));
        assertEquals("яблуко", manager.getTranslator().translatePhrase("apple"));
        assertEquals("червоне яблуко", manager.getTranslator().translatePhrase("red apple"));
    }
}