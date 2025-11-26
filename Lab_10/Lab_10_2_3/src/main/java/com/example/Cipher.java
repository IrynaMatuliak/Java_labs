package com.example;

import java.io.*;

public class Cipher {
    public static String encrypt(String input, char key) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            encrypted.append((char)(c + key));
        }
        return encrypted.toString();
    }

    public static String decrypt(String input, char key) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            decrypted.append((char)(c - key));
        }
        return decrypted.toString();
    }
}

class CipherOutputStream extends FilterOutputStream {
    private char key;

    public CipherOutputStream(OutputStream out, char key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b + key);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(b[i]);
        }
    }
}

class CipherInputStream extends FilterInputStream {
    private char key;

    public CipherInputStream(InputStream in, char key) {
        super(in);
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int data = super.read();
        return data == -1 ? -1 : data - key;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead != -1) {
            for (int i = off; i < off + bytesRead; i++) {
                b[i] = (byte)(b[i] - key);
            }
        }
        return bytesRead;
    }
}

class CipherWriter extends FilterWriter {
    private char key;

    public CipherWriter(Writer out, char key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int c) throws IOException {
        super.write(c + key);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        char[] encrypted = new char[len];
        for (int i = 0; i < len; i++) {
            encrypted[i] = (char)(cbuf[off + i] + key);
        }
        super.write(encrypted, 0, len);
    }
}

class CipherReader extends FilterReader {
    private char key;

    public CipherReader(Reader in, char key) {
        super(in);
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int data = super.read();
        return data == -1 ? -1 : data - key;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int charsRead = super.read(cbuf, off, len);
        if (charsRead != -1) {
            for (int i = off; i < off + charsRead; i++) {
                cbuf[i] = (char)(cbuf[i] - key);
            }
        }
        return charsRead;
    }
}