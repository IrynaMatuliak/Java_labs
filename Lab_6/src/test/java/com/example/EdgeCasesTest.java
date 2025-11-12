package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeCasesTest {
    private Translator translator;

    @BeforeEach
    public void setUp() {
        translator = new Translator();
    }

    @Test
    public void testTranslationWithApostrophes() {
        translator.addWordPair("don't", "не роби");
        String result = translator.translatePhrase("don't worry");
        assertTrue(result.contains("не роби") || result.contains("[worry?]"));
    }

    @Test
    public void testVeryLongPhrase() {
        String longPhrase = "this is a very long phrase that contains multiple words some existing some not";
        String result = translator.translatePhrase(longPhrase);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testNumbersInPhrase() {
        String result = translator.translatePhrase("hello 123 world 456");
        assertEquals("привіт світ", result.trim());
    }

    @Test
    public void testMixedCasePhrase() {
        String result = translator.translatePhrase("HeLLo WoRLd");
        assertEquals("привіт світ", result);
    }

    @Test
    public void testDuplicateWords() {
        String result = translator.translatePhrase("hello hello world world");
        assertEquals("привіт привіт світ світ", result);
    }

    @Test
    public void testSingleCharacterWord() {
        translator.addWordPair("a", "артикль");
        String result = translator.translatePhrase("a");
        assertEquals("артикль", result);
    }

    @Test
    public void testWhitespaceOnly() {
        String result = translator.translatePhrase("   ");
        assertEquals("[Порожня фраза]", result);
    }
}