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
    
    public static void main(String[] args){
        try {
            startGameMenu();
        } catch (RuntimeException e){
            System.out.println("Unhandled error: " + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
            System.out.println("Program stopped");
        }
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

        while (!isGameOver(secretWord)){
            printGameState(secretWord);
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

    private static void printGameState(String secretWord){
        System.out.println(showHiddenWord(secretWord));
        drawHangman();
        System.out.println("Использованные буквы: " + usedLetters);
        System.out.println("Введите букву из русского алфавита:");
    }

    private static void processGuess(String secretWord){
        Character playerGuess = readInputLetter();
        usedLetters.add(playerGuess);
        if (!isCorrectGuess(playerGuess, secretWord)){
            mistakesCount++;
            if (mistakesCount < MAX_MISTAKES){
                System.out.println("Вы не угадали букву!");
                System.out.println("Осталось попыток: " + (MAX_MISTAKES - mistakesCount));
            }
        } else {
            System.out.println("Отлично! Вы угадали букву");
        }
    }

    private static String getRandomWord(){
        String fileName = "Words.txt";
        File file = new File(fileName);
        String chosenWord = null;
        int count = 0;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String word = fileScanner.nextLine().trim();

                if (word.isEmpty()) continue;
                count++;

                if (random.nextInt(count) == 0) {
                    chosenWord = word;
                }
            }
        } catch (FileNotFoundException e){
            throw new RuntimeException(
                    "Failed to load word dictionary file. File not found: " + file.getAbsolutePath(), e);
        }

        if (chosenWord == null) {
            throw new RuntimeException(
            "Dictionary file is empty: " + file.getAbsolutePath());
        }

        return chosenWord;
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

    private static boolean isCorrectGuess(Character playerGuess, String word) {
        return word.contains(String.valueOf(playerGuess));
    }

    private static String showHiddenWord(String word) {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (usedLetters.contains(c)) {
                display.append(c);
            } else {
                display.append('*');
            }
        }

        return display.toString();
    }

    private static void drawHangman(){
        System.out.println(drawHangmanStages[mistakesCount]);
    }

    private static final String[] drawHangmanStages = {
        """
           -----
           |   |
           |
           |
           |
           |
        =========
        """,
        """
           -----
           |   |
           |   O
           |
           |
           |
        =========
        """,
        """
           -----
           |   |
           |   O
           |   |
           |
           |
        =========
        """,
        """
           -----
           |   |
           |   O
           |  /|
           |
           |
        =========
        """,
        """
           -----
           |   |
           |   O
           |  /|\\
           |
           |
        =========
        """,
        """
           -----
           |   |
           |   O
           |  /|\\
           |  /
           |
        =========
        """,
        """
           -----
           |   |
           |   O
           |  /|\\
           |  / \\
           |
        =========
        """
    };
}





