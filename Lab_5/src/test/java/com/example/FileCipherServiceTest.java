package com.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

class FileCipherServiceTest {

    @TempDir
    Path tempDir;

    private FileCipherService cipherService;
    private File testFile;
    private File encryptedFile;

    @BeforeEach
    void setUp() throws IOException {
        cipherService = new FileCipherService();
        testFile = tempDir.resolve("test.txt").toFile();
        encryptedFile = tempDir.resolve("encrypted.dat").toFile();
        String content = "Hello World!\nThis is a test file.\nJava Programming 123";
        Files.write(testFile.toPath(), content.getBytes());
    }

    @Test
    void testEncryptDecryptFileByteStreams() throws IOException {
        char key = 'K';
        cipherService.encryptFile(testFile.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        assertTrue(encryptedFile.exists());
        assertTrue(encryptedFile.length() > 0);
        File decryptedFile = tempDir.resolve("decrypted.txt").toFile();
        cipherService.decryptFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);
        assertTrue(decryptedFile.exists());
        String originalContent = new String(Files.readAllBytes(testFile.toPath()));
        String decryptedContent = new String(Files.readAllBytes(decryptedFile.toPath()));
        assertEquals(originalContent, decryptedContent);
    }

    @Test
    void testEncryptDecryptTextFileCharacterStreams() throws IOException {
        char key = 'T';
        cipherService.encryptTextFile(testFile.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        assertTrue(encryptedFile.exists());
        File decryptedFile = tempDir.resolve("decrypted_text.txt").toFile();
        cipherService.decryptTextFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);
        assertTrue(decryptedFile.exists());
        String originalContent = new String(Files.readAllBytes(testFile.toPath()));
        String decryptedContent = new String(Files.readAllBytes(decryptedFile.toPath()));
        assertEquals(originalContent, decryptedContent);
    }

    @Test
    void testEncryptDecryptStringUsingCipherClass() {
        char key = 'S';
        String originalText = "Hello World! This is a test string.";
        String encrypted = Cipher.encrypt(originalText, key);
        assertNotNull(encrypted);
        assertNotEquals(originalText, encrypted);
        String decrypted = Cipher.decrypt(encrypted, key);
        assertEquals(originalText, decrypted);
    }

    @Test
    void testEncryptDecryptEmptyStringUsingCipherClass() {
        char key = 'E';
        String originalText = "";
        String encrypted = Cipher.encrypt(originalText, key);
        String decrypted = Cipher.decrypt(encrypted, key);
        assertEquals(originalText, decrypted);
    }

    @Test
    void testEncryptDecryptWithDifferentKeysUsingCipherClass() {
        char key1 = 'A';
        char key2 = 'B';
        String originalText = "Test string for different keys";
        String encryptedWithKey1 = Cipher.encrypt(originalText, key1);
        String encryptedWithKey2 = Cipher.encrypt(originalText, key2);
        assertNotEquals(encryptedWithKey1, encryptedWithKey2);
        String decryptedWithKey1 = Cipher.decrypt(encryptedWithKey1, key1);
        String decryptedWithKey2 = Cipher.decrypt(encryptedWithKey2, key2);
        assertEquals(originalText, decryptedWithKey1);
        assertEquals(originalText, decryptedWithKey2);
    }

    @Test
    void testEncryptFileNonExistent() {
        char key = 'K';
        assertThrows(FileNotFoundException.class, () -> {
            cipherService.encryptFile("nonexistent.txt", encryptedFile.getAbsolutePath(), key);
        });
    }

    @Test
    void testDecryptFileNonExistent() {
        char key = 'K';
        assertThrows(FileNotFoundException.class, () -> {
            cipherService.decryptFile("nonexistent.txt", encryptedFile.getAbsolutePath(), key);
        });
    }

    @Test
    void testEncryptDecryptBinaryFile() throws IOException {
        File binaryFile = tempDir.resolve("binary.dat").toFile();
        byte[] binaryData = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        Files.write(binaryFile.toPath(), binaryData);
        char key = 'B';
        cipherService.encryptFile(binaryFile.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        File decryptedFile = tempDir.resolve("decrypted_binary.dat").toFile();
        cipherService.decryptFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);
        byte[] originalData = Files.readAllBytes(binaryFile.toPath());
        byte[] decryptedData = Files.readAllBytes(decryptedFile.toPath());
        assertArrayEquals(originalData, decryptedData);
    }

    @Test
    void testEncryptDecryptWithSpecialCharacters() throws IOException {
        File specialFile = tempDir.resolve("special.txt").toFile();
        String content = "Special chars: áéíóú ñ € § ® © ™";
        Files.write(specialFile.toPath(), content.getBytes("UTF-8"));
        char key = 'X';
        cipherService.encryptTextFile(specialFile.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        File decryptedFile = tempDir.resolve("decrypted_special.txt").toFile();
        cipherService.decryptTextFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);
        String originalContent = new String(Files.readAllBytes(specialFile.toPath()), "UTF-8");
        String decryptedContent = new String(Files.readAllBytes(decryptedFile.toPath()), "UTF-8");
        assertEquals(originalContent, decryptedContent);
    }

    @Test
    void testLargeFileEncryption() throws IOException {
        File largeFile = tempDir.resolve("large.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(largeFile))) {
            for (int i = 0; i < 1000; i++) {
                writer.println("Line " + i + ": This is a test line for large file encryption.");
            }
        }
        char key = 'L';
        cipherService.encryptFile(largeFile.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        File decryptedFile = tempDir.resolve("decrypted_large.txt").toFile();
        cipherService.decryptFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);
        String originalContent = new String(Files.readAllBytes(largeFile.toPath()));
        String decryptedContent = new String(Files.readAllBytes(decryptedFile.toPath()));
        assertEquals(originalContent, decryptedContent);
    }

    @Test
    void testZeroKeyEncryptionUsingCipherClass() {
        char key = 0;
        String originalText = "Test with zero key";
        String encrypted = Cipher.encrypt(originalText, key);
        String decrypted = Cipher.decrypt(encrypted, key);
        assertEquals(originalText, decrypted);
        assertEquals(originalText, encrypted);
    }

    @Test
    void testSameInputOutputPaths() throws IOException {
        char key = 'P';
        File testCopy = tempDir.resolve("test_copy.txt").toFile();
        Files.copy(testFile.toPath(), testCopy.toPath());
        cipherService.encryptFile(testCopy.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        cipherService.decryptFile(encryptedFile.getAbsolutePath(), testCopy.getAbsolutePath(), key);
        String originalContent = new String(Files.readAllBytes(testFile.toPath()));
        String finalContent = new String(Files.readAllBytes(testCopy.toPath()));
        assertEquals(originalContent, finalContent);
    }

    @Test
    void testFileWithDifferentEncodings() throws IOException {
        File utf16File = tempDir.resolve("utf16.txt").toFile();
        String content = "UTF-16 encoded text: 中文 русский Español";
        Files.write(utf16File.toPath(), content.getBytes("UTF-16"));
        char key = 'U';
        cipherService.encryptTextFile(utf16File.getAbsolutePath(), encryptedFile.getAbsolutePath(), key);
        File decryptedFile = tempDir.resolve("decrypted_utf16.txt").toFile();
        cipherService.decryptTextFile(encryptedFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key);
        assertTrue(decryptedFile.exists());
        assertTrue(decryptedFile.length() > 0);
    }
}