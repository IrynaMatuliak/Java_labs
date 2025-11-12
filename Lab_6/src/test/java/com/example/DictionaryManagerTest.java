package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DictionaryManagerTest {
    private DictionaryManager dictionaryManager;

    @BeforeEach
    public void setUp() {
        dictionaryManager = new DictionaryManager();
    }

    @Test
    public void testAddWordThroughManager() {
        String input = "test:тест\nback\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        int initialSize = dictionaryManager.getTranslator().getDictionarySize();
        dictionaryManager.getTranslator().addWordPair("test", "тест");
        int newSize = dictionaryManager.getTranslator().getDictionarySize();
        assertEquals(initialSize + 1, newSize);
        assertEquals("тест", dictionaryManager.getTranslator().translatePhrase("test"));
    }

    @Test
    public void testTranslateThroughManager() {
        String result = dictionaryManager.getTranslator().translatePhrase("hello");
        assertEquals("привіт", result);
    }

    @Test
    public void testGetTranslator() {
        Translator translator = dictionaryManager.getTranslator();
        assertNotNull(translator);
        assertTrue(translator instanceof Translator);
    }

    @Test
    public void testDictionaryInitialization() {
        Translator translator = dictionaryManager.getTranslator();
        assertTrue(translator.getDictionarySize() > 0);
        assertTrue(translator.isPhraseInDictionary("hello"));
        assertTrue(translator.isPhraseInDictionary("good morning"));
    }

    @Test
    public void testShowAllDictionaryNotEmpty() {
        assertDoesNotThrow(() -> dictionaryManager.showAllDictionary());
    }
}