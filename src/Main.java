//Проект "Виселица"

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private static Scanner scanner1 = new Scanner(System.in);  //сканнер для ввода
    private static Random random = new Random();           // для генерации чисел
    private static List<String> guessesList = new ArrayList<>();       //список всех попыток пользователя отгадать буквы
    private static String GAME_STATE_WIN = "Игрок победил";      //состояние игры если игрок победил
    private static String GAME_STATE_LOSE = "Игрок проиграл";    //состояние игры если игрок проиграл
    private static String GAME_STATE_NOT_FINISHED = "Игра еще не закончена"; //состояние игры если она еще не закончена
    private static int gameMistakesNumber = 6;                  //количество допустимых ошибок для пользователя
    private static int mistakesCount = 0;               //подсчет всех ошибок


    public static void main(String[] args) throws FileNotFoundException {

//        System.out.println(getRandomWord());
        startGameLoop();

    }

    public static void startGameLoop() throws FileNotFoundException {       //метод для бесконечного цикла игровых раундов
        while (true) {
            System.out.println("Введите [1] для новой игры, или [2] для выхода");
            String input = scanner1.nextLine();        //получаем ввод пользователя
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
        mistakesCount = 0;      //очищаем количество ошибок пользователя
        guessesList.clear();    //очищаем список использованных букв пользователя

        String wordMain = getRandomWord().toUpperCase();          //получаем рандомное слово из файла
        System.out.println(wordMain);           //вывод слова (позже УДАЛИТЬ СТРОКУ)
        System.out.println(showHiddenWord(wordMain));          //отображаем скрытое слово в виде звездочек
        System.out.println("Введите букву из русского алфавита:");


        while (Objects.equals(checkGameState(wordMain), GAME_STATE_NOT_FINISHED)) {      //бесконечный цикл
            String playerGuess = getInputLetter();      //присваиваем ввод пользователя в строку playerGuess
            guessesList.add(playerGuess);      //добавляем букву в массив всех попыток пользователя

            if (checkPlayerGuess(playerGuess, wordMain)) {       //если пользователь угадал букву
                System.out.println(showHiddenWord(wordMain));                       //отображаем текущее состояние слова
            } else {                                //если пользователь не угадал
                System.out.println(showHiddenWord(wordMain));           //отображаем текущее состояние слова
                mistakesCount++;                    //инкрементируем количество ошибок пользователя
                if (mistakesCount < 6) {
                    System.out.println("Вы не угадали (но сдаваться не стоит).");
                    System.out.println("Количество оставшихся попыток: " + (gameMistakesNumber - mistakesCount));
                    System.out.println("Вы использовали буквы: " + guessesList);    //выводим список использованных букв
                    System.out.println("\nВведите букву из русского алфавита:");
                }

            }
        }

        if (Objects.equals(checkGameState(wordMain), GAME_STATE_WIN)){       //если игрок победил
            System.out.println("Вы победили! Загаданное слово: " + wordMain);
        }

        if (Objects.equals(checkGameState(wordMain), GAME_STATE_LOSE)){       //если игрок проиграл
            System.out.println("Вы проиграли! Загаданное слово: " + wordMain);
        }


    }

    public static String getRandomWord() throws FileNotFoundException { //исключение если файл не найден

        String fileName = "WordsStockRus.txt"; //путь к файлу
        File file = new File(fileName);   //сохраняем файл
        String chosenWord = null;      //строка для сохранения полученного слова

        int count = 0;                                  //итератор для посчета слов
        try (Scanner scanner2 = new Scanner(file)) {      //пробуем считать с файла
            while (scanner2.hasNextLine()) {             //пока в файле есть слова
                String word = scanner2.nextLine().trim();       //считываем слово в строке

                if (word.isEmpty()) continue;                      //если строка пустая - пропускаем
                count++;                                        //подсчитываем количество слов

                if (random.nextInt(count) == 0) {               //каждый раз с небольшой вероятностью заменяем выбранное слово на новое
                    chosenWord = word;                        //сохраняем слово
                }
            }
        }

        if (chosenWord == null) return "Нет слов в файле!";        //нет выбранного слова - в файле нет слов
        return chosenWord;             //возвращаем сгенерированное слово
    }

    public static String getInputLetter() {        //метод для получения введеной пользователем буквы и ее проверки

        while (true) {
            String input = scanner1.nextLine().trim();      //получение введеного символа в строку input

            if (input.isEmpty()) {                               //проверка на пустое значение
                System.out.println("Пустое значение! Введите букву!");
                continue;
            }

            if (input.length() != 1) {                       //проверка на кол-во символов
                System.out.println("Введите не больше 1 буквы!");
                continue;
            }

            String letter = input.toUpperCase();        //приведение буквы к верхнему регистру
            if (!letter.matches("[А-ЯЁ]")) {      //проверка только русских букв
                System.out.println("Вы ввели неправильный символ! Введите русскую букву!");
                continue;
            }

            if (guessesList.contains(letter)) {
                System.out.println("Вы уже вводили эту букву! Введите новую!");
                continue;
            }

            return letter;                          //возвращаем букву
        }
    }

    public static boolean checkPlayerGuess(String playerGuess, String word) {
        return word.contains(playerGuess);      // проверяем, есть ли буква пользователя в загаданном слове
    }

    public static String showHiddenWord(String word) {  //метод для отображения слова в виде звездочек и с отгаданными буквами
        StringBuilder display = new StringBuilder();       //создаем пустой stringBuilder
        for (int i = 0; i < word.length(); i++) {       //цикл по длине слова
            char c = word.charAt(i);                    //берем символ слова
            if (guessesList.contains(String.valueOf(c))) {  //если лист попыток уже содержит букву
                display.append(c);                  //отображаем ее
            } else {
                display.append('*');        //иначе скрываем под звездочкой
            }
        }

        return display.toString(); //получаем строку
    }



    public static String checkGameState(String word) {

        if (mistakesCount >= gameMistakesNumber){   //если количество ошибок 6 и более - поражение
            return GAME_STATE_LOSE;
        }

        for (char c : word.toCharArray()) {   // проходим по каждой букве загаданного слова
            if (!guessesList.contains(String.valueOf(c))) {   //если в списке попыток нету хотя бы одной буквы слова - игра еще не завершена
                return GAME_STATE_NOT_FINISHED;
            }
        }

        return GAME_STATE_WIN;          //если цикл не нашел неотгаданных букв - игрок победил

        }

}

    //метод getRandomWord, который заходит в файл и берет рандомное слово
    //метод getInputLetter для получения и валидации буквы, вводимой пользователем
    //метод startGameRound для начала раунда
    //метод startGameLoop для начала цикла игры
    //метод checkGameState для проверки состояния игры
    //метод checkPlayerGuess для проверки угадал ли пользователь букву
    //метод drawHangman для отрисовки виселицы



