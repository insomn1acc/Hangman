//Проект "Виселица"

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;


public class Main {

    private static Scanner scanner1 = new Scanner(System.in);  //сканнер для ввода
    private static Random random = new Random();           // для генерации чисел
    private static final int ALL_WORDS_AMOUNT = 11650; // количество слов в txt файле
    private static List<String> guessesList = new ArrayList<>();       //список всех попыток пользователя отгадать буквы
    public static void main(String[] args) throws FileNotFoundException {

        System.out.println(getRandomWord());


    }

    public static void startGameLoop() {

    }

    public static String getRandomWord() throws FileNotFoundException { //исключение если файл не найден

        String fileName = "WordsStockRus.txt"; //путь к файлу
        File file = new File(fileName);   //сохраняем файл

        int randomWordIndex = random.nextInt(ALL_WORDS_AMOUNT); //генерируем случайный индекс слова для игры

        String[] words = new String[ALL_WORDS_AMOUNT];  //массив для слов из файла
        int count = 0;                                  //итератор для цикла сохранения слов в массив
        try (Scanner scanner2 = new Scanner(file)){      //пробуем считать с файла
            while (scanner2.hasNextLine() && count <= ALL_WORDS_AMOUNT){ //пока в файле есть слова + пока итератор меньше кол-ва слов
                String word = scanner2.nextLine().trim();       //считываем слово в строке
                if (!word.isEmpty()){                           //проверка пустая ли строка
                    words[count++] = word;              //если не пустая - добавляем в массив
                }
            }
        }

        if (count == 0) return "Нет слов в файле!";        //итератор остался 0 - в файле нет слов
        return words[randomWordIndex];             //возвращаем сгенерированное слово
    }

    public static String getInputLetter(){        //метод для получения введеной пользователем буквы и ее проверки

        System.out.println("Введите букву");

        do {
            String input = scanner1.nextLine().trim();      //получение введеного символа в строку input

            if (input.isEmpty()){                               //проверка на пустое значение
                System.out.println("Пустое значение! Введите букву!");
                continue;
            }

            if (input.length() != 1){                       //проверка на кол-во символов
                System.out.println("Введите не больше 1 буквы!");
                continue;
            }

            char inputChar = input.charAt(0);               //приводим строку к символу
            if (!Character.isLetter(inputChar)){               //проверка является ли введеной символ буквой
                System.out.println("Вы ввели неправильный символ! Введите букву!");
                continue;
            }

            String letter = input.toLowerCase();        //приводим к нижнему регистру

            if (guessesList.contains(letter)){
                System.out.println("Вы уже вводили эту букву! Введите новую!");
                continue;
            }

            guessesList.add(letter);                     //добавляем букву в массив всех попыток пользователя

            return letter;                          //возвращаем букву в нижнем регистре

        } while (true);
    }

    //метод getRandomWord, который заходит в файл и берет рандомное слово
    //метод getInputLetter для получения и валидации буквы, вводимой пользователем
    //метод startGameRound для начала раунда
    //метод startGameLoop для начала цикла игры
    //метод checkGameState для проверки состояния игры
    //метод checkLetterGuess для проверки угадал ли пользователь букву
    //метод drawHangman для отрисовки виселицы
}

