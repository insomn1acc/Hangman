package main.java.ru.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private final static Scanner scanner1 = new Scanner(System.in);
    private final static Random random = new Random();
    private static List<String> guessesList = new ArrayList<>();
    private final static String GAME_STATE_WIN = "Игрок победил";
    private final static String GAME_STATE_LOSE = "Игрок проиграл";
    private final static String GAME_STATE_NOT_FINISHED = "Игра еще не закончена";
    private final static int gameMistakesNumber = 6;
    private static int mistakesCount = 0;


    public static void main(String[] args) throws FileNotFoundException {

        startGameLoop();
    }

    public static void startGameLoop() throws FileNotFoundException {
        while (true) {
            System.out.println("Введите [1] для новой игры, или [2] для выхода");
            String input = scanner1.nextLine();
            if (input.equals("1")) {
                startGameRound();
            } else if (input.equals("2")) {
                System.out.println("Выход из игры...");
                break;
            } else {
                System.out.println("Ошибка! Введите цифру [1] или [2].");
            }
        }
    }


    public static void startGameRound() throws FileNotFoundException {
        mistakesCount = 0;
        guessesList.clear();

        String wordMain = getRandomWord().toUpperCase();
        System.out.println(showHiddenWord(wordMain));
        System.out.println("Введите букву из русского алфавита:");

        String gameState = checkGameState(wordMain);


        while (gameState.equals(GAME_STATE_NOT_FINISHED)) {
            String playerGuess = getInputLetter();
            guessesList.add(playerGuess);

            if (checkPlayerGuess(playerGuess, wordMain)) {
                System.out.println(showHiddenWord(wordMain));

            } else {
                System.out.println(showHiddenWord(wordMain));
                mistakesCount++;
                drawHangman();
                if (mistakesCount < gameMistakesNumber) {
                    System.out.println("Вы не угадали (но сдаваться не стоит).");
                    System.out.println("Количество оставшихся попыток: " + (gameMistakesNumber - mistakesCount));
                    System.out.println("Вы использовали буквы: " + guessesList);

                }
            }
            gameState = checkGameState(wordMain);

            if (gameState.equals(GAME_STATE_NOT_FINISHED)){
                System.out.println("\nВведите букву из русского алфавита:");
            }
        }

        if (gameState.equals(GAME_STATE_WIN)) {
            System.out.println("\nВы победили! Загаданное слово: " + wordMain);
        }

        if (gameState.equals(GAME_STATE_LOSE)) {
            System.out.println("\nВы проиграли! Загаданное слово: " + wordMain);
        }


    }

    public static String getRandomWord() throws FileNotFoundException {

        String fileName = "WordsStockRus.txt";
        File file = new File(fileName);
        String chosenWord = null;

        int count = 0;
        try (Scanner scanner2 = new Scanner(file)) {
            while (scanner2.hasNextLine()) {
                String word = scanner2.nextLine().trim();

                if (word.isEmpty()) continue;
                count++;

                if (random.nextInt(count) == 0) {
                    chosenWord = word;
                }
            }
        }

        if (chosenWord == null) return "Нет слов в файле!";
        return chosenWord;
    }

    public static String getInputLetter() {

        while (true) {
            String input = scanner1.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Пустое значение! Введите букву!");
                continue;
            }

            if (input.length() != 1) {
                System.out.println("Введите не больше 1 буквы!");
                continue;
            }

            String letter = input.toUpperCase();
            if (!letter.matches("[А-ЯЁ]")) {
                System.out.println("Вы ввели неправильный символ! Введите русскую букву!");
                continue;
            }

            if (guessesList.contains(letter)) {
                System.out.println("Вы уже вводили эту букву! Введите новую!");
                continue;
            }

            return letter;
        }
    }

    public static boolean checkPlayerGuess(String playerGuess, String word) {
        return word.contains(playerGuess);
    }

    public static String showHiddenWord(String word) {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (guessesList.contains(String.valueOf(c))) {
                display.append(c);
            } else {
                display.append('*');
            }
        }

        return display.toString();
    }


    public static String checkGameState(String word) {

        if (mistakesCount >= gameMistakesNumber) {
            return GAME_STATE_LOSE;
        }

        for (char c : word.toCharArray()) {
            if (!guessesList.contains(String.valueOf(c))) {
                return GAME_STATE_NOT_FINISHED;
            }
        }

        return GAME_STATE_WIN;

    }

    public static void drawHangman(){
        System.out.println(drawHangmanStages[mistakesCount]);
    }

    public static String[] drawHangmanStages = {
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





