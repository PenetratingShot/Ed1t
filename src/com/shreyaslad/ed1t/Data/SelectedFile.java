package com.shreyaslad.ed1t.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SelectedFile {

    private static String selectedFilePath;

    public static String getSelectedFilePath() {
        return selectedFilePath;
    }

    public static void setSelectedFilePath(String path) {
        selectedFilePath = path;
    }

    public static String getFileContents() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFilePath))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }

            return stringBuilder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

