/**
 * Класс главного меню прототипа
 * @autor Павел Игушкин, p@devlpr.ru, Telegram: @igush
 */

import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        { // Заполнение памяти программы случайной статистикой для теста
            fillRandomStatistics("2022-05", 20, 9000, 13000);
            fillRandomStatistics("2022-04", 9000, 13000);
            fillRandomStatistics("2022-03", 9000, 13000);
        }
        menu(); // главное меню
    }

    /**
     * Метод вывода главного меню
     */
    public static void menu() {
        while (true) {
            System.out.println("""
                    Введите команду:
                    1 — ввести количество шагов
                    2 — показать статистику
                    3 — настроить ежедневную цель
                    4 — выйти""");
            Scanner scanner = new Scanner(System.in);
            int command;
            if (scanner.hasNextInt()) { // Если введено целое число
                command = scanner.nextInt();
                if (command == 1) { // команда 1 — ввести количество шагов
                    numberOfSteps();
                } else if (command == 2) { // команда 2 — показать статистику
                    statistics();
                } else if (command == 3) { // команда 3 — настроить ежедневную цель
                    dailyGoal();
                } else if (command == 4) { // команда 4 — выйти
                    System.out.println("Хороших шагов!\n" +
                            "Программа завершена");
                    return;
                } else {
                    System.err.println("Введите номер команды из предложенных.");
                }
            } else { // Если введено не целое число
                System.err.println("Команды нужно вводить числом.");
            }
        }
    }

    /**
     * Метод обработки команды 1 — ввести количество шагов
     */
    public static void numberOfSteps() {
        int command;
        String date; // дата
        int numberOfSteps; // шагов за эту дату
        while (true) {
            System.out.println("За какой день ввести количество шагов?");
            System.out.println("""
                    1 — за сегодня
                    2 — за вчера
                    3 — за другую дату
                    4 — назад""");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                command = scanner.nextInt();

                // сегодняшняя дата в формате 2022-05-04 (4 мая 2022)
                LocalDate todayDate = LocalDate.now();

                // вчерашняя дата в формате 2022-05-03 (3 мая 2022)
                LocalDate yesterdayDate = LocalDate.of(todayDate.getYear(), todayDate.getMonth(), todayDate.getDayOfMonth() - 1);

                if (command == 1) { // Команда 1 — ввод данных за сегодня
                    date = String.valueOf(todayDate);
                    System.out.println("Выбрана сегодняшняя дата — " + date + ".");
                    StepTracker.inputSteps(date);
                    continueProgramExecution();
                    break;
                } else if (command == 2) { // Команда 2 — ввод данных за вчера
                    date = String.valueOf(yesterdayDate);
                    System.out.println("Выбрана вчерашняя дата — " + date + ".");
                    StepTracker.inputSteps(date);
                    continueProgramExecution();
                    break;
                } else if (command == 3) { // Команда 3 — ввод данных за другую дату
                    StepTracker.inputSteps();
                    continueProgramExecution();
                    break;
                } else if (command == 4) { // Команда 4 — назад
                    System.out.println("Ввод значений отменён.");
                    return;
                } else
                    System.err.println("Введите номер команды из предложенных.");
            } else
                System.err.println("Введите команду числом.");
        }
    }

    /**
     * Метод обработки команды 2 — показать статистику
     */
    public static void statistics() {
        int command;
        while (true) {
            System.out.println("За какой месяц показать статистику?");
            System.out.println("""
                    1 — за этот
                    2 — за прошлый
                    3 — выбрать месяц
                    4 — назад""");
            String month;
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                command = scanner.nextInt();
                if (command == 1) { // 1 — за этот месяц
                    month = StepTracker.getThisMonth();
                    if (!(StepTracker.months.containsKey(month))) { // если за текущий месяц нет статистики
                        System.err.println("За текущий месяц ещё нет статистики.");
                        statistics();
                    } else {
                        StepTracker.printOneMonth(month);
                        continueProgramExecution();
                    }
                } else if (command == 2) { // 2 - за прошлый месяц
                    month = StepTracker.getPreviousMonth();
                    if (!(StepTracker.months.containsKey(month))) { // если за текущий месяц нет статистики
                        System.err.println("За прошлый месяц нет статистики.");
                        statistics();
                    } else {
                        StepTracker.printOneMonth(month);
                        continueProgramExecution();
                    }
                } else if (command == 3) {
                    StepTracker.printAllMonths();
                    month = StepTracker.selectMonth();
                    StepTracker.printOneMonth(month);
                    continueProgramExecution();
                    break;
                } else if (command == 4) {
                    return;
                } else
                    System.err.println("Введите номер команды из предложенных.");
            } else
                System.err.println("Введите команду числом.");
        }
    }

    /**
     *  Метод обработки команды 3 — настроить ежедневную цель
     */
    public static void dailyGoal() {
        int command;
        while (true) {
            StepTracker.printDailyGoal();
            System.out.println("1 — установить новую цель\n" +
                    "2 — назад");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                command = scanner.nextInt();
                if (command == 1) { // Команда 1 — установить новую цель
                    StepTracker.changeDailyGoal();
                    continueProgramExecution();
                    break;
                } else if (command == 2) { // Команда 2 — назад
                    System.out.println("Изменение ежедневной цели отменено.");
                    return;
                } else {
                    System.err.println("Введите номер команды из предложенных.");
                }
            } else {
                System.err.println("Введите команду числом.");
            }
        }
    }

    /**
     * Метод обработки команды «Нажмите 1, чтобы продолжить»
     */
    public static void continueProgramExecution() {
        System.out.println("1 — далее");
        int command;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                command = scanner.nextInt();
                if (command == 1)
                    return;
                else
                    System.err.println("Введите 1, чтобы продолжить"); // сообщение, если введённая команда не 1
            } else
                System.err.println("Введите 1, чтобы продолжить"); // сообщение, если введённая команда не целое число
        }
    }

    /**
     * Метод заполнения месяца рандомной статистикой для теста
     */
    public static void fillRandomStatistics(String month, int minRandomValueOfSteps, int maxRandomValueOfSteps) {
        StepTracker.addMonth(month);
        Random r = new Random();
        for (int i = 0; i < StepTracker.months.get(month).length; i++) {
            StepTracker.months.get(month)[i] = r.nextInt(minRandomValueOfSteps, maxRandomValueOfSteps);
        }
    }

    /**
     * Метод заполнения месяца рандомной статистикой для теста с указанием числа месяца, до которого нужно заполнить
     */
    public static void fillRandomStatistics(String month, int finishday, int minRandomValueOfSteps, int maxRandomValueOfSteps) {
        StepTracker.addMonth(month);
        Random r = new Random();
        for (int i = 0; i <= finishday; i++) {
            StepTracker.months.get(month)[i] = r.nextInt(minRandomValueOfSteps, maxRandomValueOfSteps);
        }
    }
}