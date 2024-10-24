package com.myapp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    public static void readMatriks(String[] args) {
        String inputfilename = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputfilename));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeMatriks() {
        int[][] X =
        {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        String outputfilename = "output.txt";
        try {
            FileWriter writer = new FileWriter(outputfilename);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (int i = 0; i < X.length; i++) {
                for (int j = 0; j < X[i].length; j++) {
                    bufferedWriter.write(X[i][j] + " ");
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTextToFile(String content, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}