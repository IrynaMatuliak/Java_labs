package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CipherTest {
    private static final String TEST_TEXT = "Hello World! Test text 123";
    private static final char TEST_KEY = 'K';
    private static final String TEST_FILE = "test_cipher.txt";
    private static final String ENCRYPTED_FILE = "test_encrypted.dat";
    private static final String DECRYPTED_FILE = "test_decrypted.txt";

    @BeforeEach
    void setUp() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_FILE))) {
            writer.println(TEST_TEXT);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
        Files.deleteIfExists(Paths.get(ENCRYPTED_FILE));
        Files.deleteIfExists(Paths.get(DECRYPTED_FILE));
    }

    @Test
    void testEncryptDecryptString() {
        System.out.println("=== String Encryption/Decryption Test ===");
        String encrypted = Cipher.encrypt(TEST_TEXT, TEST_KEY);
        assertNotNull(encrypted);
        assertNotEquals(TEST_TEXT, encrypted);
        System.out.println("Original: " + TEST_TEXT);
        System.out.println("Encrypted: " + encrypted);
        String decrypted = Cipher.decrypt(encrypted, TEST_KEY);
        assertNotNull(decrypted);
        assertEquals(TEST_TEXT, decrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("String encryption/decryption test passed\n");
    }

    @Test
    void testEmptyString() {
        System.out.println("=== Empty String Test ===");
        String empty = "";
        String encrypted = Cipher.encrypt(empty, TEST_KEY);
        String decrypted = Cipher.decrypt(encrypted, TEST_KEY);
        assertEquals(empty, decrypted);
        System.out.println("Empty string test passed\n");
    }

    @Test
    void testCipherOutputStream() throws IOException {
        System.out.println("=== CipherOutputStream Test ===");
        try (FileInputStream fis = new FileInputStream(TEST_FILE);
             CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(ENCRYPTED_FILE), TEST_KEY)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
        File encryptedFile = new File(ENCRYPTED_FILE);
        assertTrue(encryptedFile.exists());
        assertTrue(encryptedFile.length() > 0);
        System.out.println("File encrypted: " + encryptedFile.length() + " bytes");
        System.out.println("CipherOutputStream test passed\n");
    }

    @Test
    void testCipherInputStream() throws IOException {
        System.out.println("=== CipherInputStream Test ===");
        try (FileInputStream fis = new FileInputStream(TEST_FILE);
             CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(ENCRYPTED_FILE), TEST_KEY)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
        try (CipherInputStream cis = new CipherInputStream(new FileInputStream(ENCRYPTED_FILE), TEST_KEY);
             FileOutputStream fos = new FileOutputStream(DECRYPTED_FILE)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        File decryptedFile = new File(DECRYPTED_FILE);
        assertTrue(decryptedFile.exists());
        String decryptedContent = new String(Files.readAllBytes(Paths.get(DECRYPTED_FILE)));
        String originalContent = new String(Files.readAllBytes(Paths.get(TEST_FILE)));
        assertEquals(originalContent, decryptedContent);
        System.out.println("File content correctly decrypted");
        System.out.println("CipherInputStream test passed\n");
    }

    @Test
    void testCipherWriter() throws IOException {
        System.out.println("=== CipherWriter Test ===");
        try (FileReader fr = new FileReader(TEST_FILE);
             CipherWriter cw = new CipherWriter(new FileWriter(ENCRYPTED_FILE), TEST_KEY)) {
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = fr.read(buffer)) != -1) {
                cw.write(buffer, 0, charsRead);
            }
        }
        File encryptedFile = new File(ENCRYPTED_FILE);
        assertTrue(encryptedFile.exists());
        assertTrue(encryptedFile.length() > 0);
        System.out.println("Text file encrypted: " + encryptedFile.length() + " bytes");
        System.out.println("CipherWriter test passed\n");
    }

    @Test
    void testCipherReader() throws IOException {
        System.out.println("=== CipherReader Test ===");
        try (FileReader fr = new FileReader(TEST_FILE);
             CipherWriter cw = new CipherWriter(new FileWriter(ENCRYPTED_FILE), TEST_KEY)) {
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = fr.read(buffer)) != -1) {
                cw.write(buffer, 0, charsRead);
            }
        }
        try (CipherReader cr = new CipherReader(new FileReader(ENCRYPTED_FILE), TEST_KEY);
             FileWriter fw = new FileWriter(DECRYPTED_FILE)) {
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = cr.read(buffer)) != -1) {
                fw.write(buffer, 0, charsRead);
            }
        }
        File decryptedFile = new File(DECRYPTED_FILE);
        assertTrue(decryptedFile.exists());
        String decryptedContent = new String(Files.readAllBytes(Paths.get(DECRYPTED_FILE)));
        String originalContent = new String(Files.readAllBytes(Paths.get(TEST_FILE)));
        assertEquals(originalContent, decryptedContent);
        System.out.println("Text correctly decrypted");
        System.out.println("CipherReader test passed\n");
    }

    @Test
    void testDifferentKeys() {
        System.out.println("=== Different Keys Test ===");
        String encrypted = Cipher.encrypt(TEST_TEXT, 'A');
        String decryptedWithWrongKey = Cipher.decrypt(encrypted, 'B');
        assertNotEquals(TEST_TEXT, decryptedWithWrongKey);
        System.out.println("Wrong key check passed");
        System.out.println("Different keys test passed\n");
    }
}