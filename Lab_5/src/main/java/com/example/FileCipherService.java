package com.example;

import java.io.*;

public class FileCipherService {
    private static final int BUFFER_SIZE = 1024;

    public void encryptFile(String inputFileName, String outputFileName, char key) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFileName);
             CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(outputFileName), key)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public void decryptFile(String inputFileName, String outputFileName, char key) throws IOException {
        try (CipherInputStream cis = new CipherInputStream(new FileInputStream(inputFileName), key);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    public void encryptTextFile(String inputFileName, String outputFileName, char key) throws IOException {
        try (FileReader fr = new FileReader(inputFileName);
             CipherWriter cw = new CipherWriter(new FileWriter(outputFileName), key)) {
            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;
            while ((charsRead = fr.read(buffer)) != -1) {
                cw.write(buffer, 0, charsRead);
            }
        }
    }

    public void decryptTextFile(String inputFileName, String outputFileName, char key) throws IOException {
        try (CipherReader cr = new CipherReader(new FileReader(inputFileName), key);
             FileWriter fw = new FileWriter(outputFileName)) {
            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;
            while ((charsRead = cr.read(buffer)) != -1) {
                fw.write(buffer, 0, charsRead);
            }
        }
    }
}