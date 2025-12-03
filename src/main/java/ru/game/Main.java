package main.java.ru.game;

import java.util.*;


public class Main {

    private static final Scanner consoleScanner = new Scanner(System.in);
    private static final List<Character> usedLetters = new ArrayList<>();
    private static final int MAX_MISTAKES = 6;
    private static int mistakesCount = 0;
    private static final char HIDDEN_LETTER_CHAR = '*';
    private static final StringBuilder SECRET_WORD_MASK = new StringBuilder();
    
    public static void main(String[] args){
        try {
            Dictionary.loadDictionary();
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

        String secretWord = Dictionary.getRandomWord().toUpperCase();
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





