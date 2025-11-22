package main.java.ru.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private static final Scanner consoleScanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final List<Character> usedLetters = new ArrayList<>();
    private static final String GAME_STATE_WIN = "Игрок победил";
    private static final String GAME_STATE_LOSE = "Игрок проиграл";
    private static final String GAME_STATE_NOT_FINISHED = "Игра еще не закончена";
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


    private static void startGame(){
        mistakesCount = 0;
        usedLetters.clear();

        String secretWord = getRandomWord().toUpperCase();
        System.out.println(showHiddenWord(secretWord));
        System.out.println("Введите букву из русского алфавита:");

        String gameState = checkGameState(secretWord);


        while (gameState.equals(GAME_STATE_NOT_FINISHED)) {
            Character playerGuess = getInputLetter();
            usedLetters.add(playerGuess);

            if (checkPlayerGuess(playerGuess, secretWord)) {
                System.out.println(showHiddenWord(secretWord));

            } else {
                System.out.println(showHiddenWord(secretWord));
                mistakesCount++;
                drawHangman();
                if (mistakesCount < MAX_MISTAKES) {
                    System.out.println("Вы не угадали (но сдаваться не стоит).");
                    System.out.println("Количество оставшихся попыток: " + (MAX_MISTAKES - mistakesCount));
                    System.out.println("Вы использовали буквы: " + usedLetters);

                }
            }
            gameState = checkGameState(secretWord);

            if (gameState.equals(GAME_STATE_NOT_FINISHED)){
                System.out.println("\nВведите букву из русского алфавита:");
            }
        }

        if (gameState.equals(GAME_STATE_WIN)) {
            System.out.println("\nВы победили! Загаданное слово: " + secretWord);
        }
        if (gameState.equals(GAME_STATE_LOSE)) {
            System.out.println("\nВы проиграли! Загаданное слово: " + secretWord);
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

    private static Character getInputLetter() {

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

    private static boolean checkPlayerGuess(Character playerGuess, String word) {
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


    private static String checkGameState(String word) {

        if (mistakesCount >= MAX_MISTAKES) {
            return GAME_STATE_LOSE;
        }

        for (char c : word.toCharArray()) {
            if (!usedLetters.contains(c)) {
                return GAME_STATE_NOT_FINISHED;
            }
        }

        return GAME_STATE_WIN;

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





