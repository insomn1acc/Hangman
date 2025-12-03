package main.java.ru.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Dictionary {
    private static final List<String> dictionary = new ArrayList<>();
    private static final Random random = new Random();
    public static void loadDictionary(){
        dictionary.clear();

        String fileName = "Words.txt";
        File file = new File(fileName);

        if (!file.exists()) {
            throw new RuntimeException("Dictionary file not found: " + file.getAbsolutePath());
        }

        if (file.length() == 0) {
            throw new RuntimeException(
                    "Dictionary file is empty: " + file.getAbsolutePath());
        }

        try (Scanner fileScanner = new Scanner(file, "UTF-8")){
            while (fileScanner.hasNextLine()){
                String raw = fileScanner.nextLine();
                String word = normalizeLine(raw);

                if (word.isEmpty()){
                    continue;
                }

                dictionary.add(word);
            }

            dictionary.removeIf(String::isBlank);

            if (dictionary.isEmpty()){
                throw new RuntimeException(
                        "Dictionary file contains only empty lines: " + file.getAbsolutePath());
            }
        } catch (FileNotFoundException e){
            throw new RuntimeException(
                    "Failed to load word dictionary file. File not found: " + file.getAbsolutePath(), e);
        }
    }

    private static String normalizeLine(String s) {
        if (s == null) return "";
        String cleaned = s.replaceAll("[\\uFEFF\\u200B\\u200C\\u200D\\u2060]", "");
        cleaned = cleaned.trim();
        return cleaned;
    }

    public static String getRandomWord(){
        if (dictionary.isEmpty()) {
            throw new IllegalStateException("Dictionary is empty");
        }

        int wordIndex = random.nextInt(dictionary.size());
        return dictionary.get(wordIndex);
    }
}
