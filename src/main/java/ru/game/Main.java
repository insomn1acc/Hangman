package main.java.ru.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private static final Scanner consoleScanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final List<Character> usedLetters = new ArrayList<>();
    private static final int MAX_MISTAKES = 6;
    private static int mistakesCount = 0;
    private static final List<String> dictionary = new ArrayList<>();
    private static final char HIDDEN_LETTER_CHAR = '*';
    private static final StringBuilder SECRET_WORD_MASK = new StringBuilder();
    
    public static void main(String[] args){
        try {
            loadDictionary();
        } catch (RuntimeException e){
            System.out.println("Unhandled error: " + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            System.out.println("Program stopped");
            return;
        }

        startGameMenu();
    }

    private static void startGameMenu(){
        while (true) {
            System.out.println("Введите [1] для новой игры, или [2] для выхода");
            String input = consoleScanner.nextLine();
            if (input.equals("1")) {
                startGame();
            } else if (input.equals("2")) {
                System.out.println("Выход из игры...");
                break;
            } else {
                System.out.println("Ошибка! Введите цифру [1] или [2].");
            }
        }
    }

    private static boolean isGameOver(String secretWord) {
        return isLose() || isWin(secretWord);
    }

    private static boolean isWin(String secretWord) {
        for (char ch : secretWord.toCharArray()) {
            if (!usedLetters.contains(ch)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLose() {
        return mistakesCount >= MAX_MISTAKES;
    }


    private static void startGame(){
        mistakesCount = 0;
        usedLetters.clear();

        String secretWord = getRandomWord().toUpperCase();
        initializeSecretWordMask(secretWord);

        while (!isGameOver(secretWord)){
            printGameState();
            processGuess(secretWord);
        }

        if (isWin(secretWord)) {
            System.out.println("\nВы победили! Загаданное слово: " + secretWord);
        }
        if (isLose()) {
            System.out.println("\nВы проиграли! Загаданное слово: " + secretWord);
            drawHangman();
        }
    }

    private static boolean applyGuess(char ch, String word){
        boolean hit = word.contains(String.valueOf(ch));
        if (!hit){
            mistakesCount++;
        } else {
            openLetter(ch, word);
        }
        return hit;
    }

    private static void showGuessResult(boolean success){
        if (success){
            System.out.println("Отлично! Вы угадали!");
        } else {
            System.out.println("Вы не угадали!");
        }
    }

    private static void printGameState(){
        System.out.println(SECRET_WORD_MASK);
        drawHangman();
        System.out.println("Использованные буквы: " + usedLetters);
        System.out.println("Введите букву из русского алфавита:");
    }

    private static void processGuess(String secretWord){
        char playerGuess = readInputLetter();
        usedLetters.add(playerGuess);

        boolean success = applyGuess(playerGuess, secretWord);
        showGuessResult(success);

        if (mistakesCount < MAX_MISTAKES){
            System.out.println("Осталось попыток: " + (MAX_MISTAKES - mistakesCount));
        }
    }

    private static void loadDictionary(){
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

    private static String getRandomWord(){
        if (dictionary.isEmpty()) {
            throw new IllegalStateException("Dictionary is empty");
        }

        int wordIndex = random.nextInt(dictionary.size());
        return dictionary.get(wordIndex);
    }

    private static Character readInputLetter() {

        while (true) {
            String input = consoleScanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Пустое значение! Введите букву!");
                continue;
            }

            if (input.length() != 1) {
                System.out.println("Введите не больше 1 буквы!");
                continue;
            }

            char letter = Character.toUpperCase(input.charAt(0));

            if ((letter < 'А' || letter > 'Я') && letter != 'Ё') {
                System.out.println("Вы ввели неправильный символ! Введите русскую букву!");
                continue;
            }

            if (usedLetters.contains(letter)) {
                System.out.println("Вы уже вводили эту букву! Введите новую!");
                continue;
            }

            return letter;
        }
    }

    private static void initializeSecretWordMask(String secretWord){
        SECRET_WORD_MASK.setLength(0);
        for (int i = 0; i < secretWord.length(); i++){
            SECRET_WORD_MASK.append(HIDDEN_LETTER_CHAR);
        }
    }

    private static void openLetter(char letter, String secretWord){
        for (int i = 0; i < secretWord.length(); i++){
            if (secretWord.charAt(i) == letter){
                SECRET_WORD_MASK.setCharAt(i, letter);
            }
        }
    }

    private static void drawHangman(){
        if (mistakesCount < 0 || mistakesCount > 6){
            throw new IllegalArgumentException("Invalid hangman stage: " + mistakesCount);
        }
        HangmanRenderer.render(mistakesCount);
    }
}





