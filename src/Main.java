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
    private static List<String> guessesList = new ArrayList<>();       //список всех попыток пользователя отгадать буквы


    public static void main(String[] args) throws FileNotFoundException {

        System.out.println(getRandomWord());
        startGameRound();

    }

    public static void startGameLoop() {

    }

    public static void startGameRound(){
        String x = getInputLetter();
        guessesList.add(x);      //добавляем букву в массив всех попыток пользователя
        System.out.println(guessesList);
    }

    public static String getRandomWord() throws FileNotFoundException { //исключение если файл не найден

        String fileName = "WordsStockRus.txt"; //путь к файлу
        File file = new File(fileName);   //сохраняем файл
        String chosenWord = null;      //строка для сохранения полученного слова

        int count = 0;                                  //итератор для посчета слов
        try (Scanner scanner2 = new Scanner(file)){      //пробуем считать с файла
            while (scanner2.hasNextLine()){             //пока в файле есть слова
                String word = scanner2.nextLine().trim();       //считываем слово в строке

                if (word.isEmpty()) continue;                      //если строка пустая - пропускаем
                count++;                                        //подсчитываем количество слов

                if (random.nextInt(count) == 0){               //каждый раз с небольшой вероятностью заменяем выбранное слово на новое
                    chosenWord = word;                        //сохраняем слово
                }
            }
        }

        if (chosenWord == null) return "Нет слов в файле!";        //нет выбранного слова - в файле нет слов
        return chosenWord;             //возвращаем сгенерированное слово
    }

    public static String getInputLetter(){        //метод для получения введеной пользователем буквы и ее проверки

        System.out.println("Введите русскую букву");

        while (true){
            String input = scanner1.nextLine().trim();      //получение введеного символа в строку input

            if (input.isEmpty()){                               //проверка на пустое значение
                System.out.println("Пустое значение! Введите букву!");
                continue;
            }

            if (input.length() != 1){                       //проверка на кол-во символов
                System.out.println("Введите не больше 1 буквы!");
                continue;
            }

            String letter = input.toLowerCase();        //приведение буквы к нижнему регистру
            if (!letter.matches("[а-яё]")){      //проверка только русских букв
                System.out.println("Вы ввели неправильный символ! Введите русскую букву!");
                continue;
            }

            if (guessesList.contains(letter)){
                System.out.println("Вы уже вводили эту букву! Введите новую!");
                continue;
            }

            return letter;                          //возвращаем букву в нижнем регистре

        }
    }

    //метод getRandomWord, который заходит в файл и берет рандомное слово
    //метод getInputLetter для получения и валидации буквы, вводимой пользователем
    //метод startGameRound для начала раунда
    //метод startGameLoop для начала цикла игры
    //метод checkGameState для проверки состояния игры
    //метод checkLetterGuess для проверки угадал ли пользователь букву
    //метод drawHangman для отрисовки виселицы
}

