import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        {
            fillRandomStatistics("2022-05", 20, 9000, 13000);
            fillRandomStatistics("2022-04", 9000, 13000);
            fillRandomStatistics("2022-03", 9000, 13000);
        }
        menu();
    }

    private static void menu() {
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
                    enterNumberOfSteps();
                } else if (command == 2) { // команда 2 — показать статистику
                    showStatistics();
                } else if (command == 3) { // команда 3 — настроить ежедневную цель
                    setDailyGoal();
                } else if (command == 4) { // команда 4 — выйти
                    System.out.println("Хороших шагов!\n" +
                            "Программа завершена");
                    return;
                } else {
                    System.out.println("[!] Введите номер команды из предложенных.");
                }
            } else { // Если введено не целое число
                System.out.println("[!] Команды нужно вводить числом.");
            }
        }
    }

    private static void enterNumberOfSteps() {
        int command;
        String date;
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

                LocalDate todayDate = LocalDate.now(); // format: 2022-05-04 (4 мая 2022)
                LocalDate yesterdayDate = LocalDate.of(todayDate.getYear(), todayDate.getMonth(), todayDate.getDayOfMonth() - 1);

                if (command == 1) { // Команда 1 — ввод данных за сегодня
                    date = String.valueOf(todayDate);
                    System.out.println("Выбрана сегодняшняя дата — " + date + ".");
                    StepTracker.inputSteps(date);
                    pause();
                    break;
                } else if (command == 2) { // Команда 2 — ввод данных за вчера
                    date = String.valueOf(yesterdayDate);
                    System.out.println("Выбрана вчерашняя дата — " + date + ".");
                    StepTracker.inputSteps(date);
                    pause();
                    break;
                } else if (command == 3) { // Команда 3 — ввод данных за другую дату
                    StepTracker.inputSteps();
                    pause();
                    break;
                } else if (command == 4) { // Команда 4 — назад
                    System.out.println("Ввод значений отменён.");
                    return;
                } else
                    System.out.println("[!] Введите номер команды из предложенных.");
            } else
                System.out.println("[!] Введите команду числом.");
        }
    }

    private static void showStatistics() {
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
                        System.out.println("[!] За текущий месяц ещё нет статистики.");
                        showStatistics();
                    } else {
                        StepTracker.printStatisticsForOneMonth(month);
                        pause();
                    }
                } else if (command == 2) { // 2 - за прошлый месяц
                    month = StepTracker.getPreviousMonth();
                    if (!(StepTracker.months.containsKey(month))) { // если за текущий месяц нет статистики
                        System.out.println("[!] За прошлый месяц нет статистики.");
                        showStatistics();
                    } else {
                        StepTracker.printStatisticsForOneMonth(month);
                        pause();
                    }
                } else if (command == 3) {
                    StepTracker.printAllMonths();
                    month = StepTracker.selectMonth();
                    StepTracker.printStatisticsForOneMonth(month);
                    pause();
                    break;
                } else if (command == 4) {
                    return;
                } else
                    System.out.println("[!] Введите номер команды из предложенных.");
            } else
                System.out.println("[!] Введите команду числом.");
        }
    }

    private static void setDailyGoal() {
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
                    pause();
                    break;
                } else if (command == 2) { // Команда 2 — назад
                    System.out.println("Изменение ежедневной цели отменено.");
                    return;
                } else {
                    System.out.println("[!] Введите номер команды из предложенных.");
                }
            } else {
                System.out.println("[!] Введите команду числом.");
            }
        }
    }

    private static void pause() {
        System.out.println("1 — далее");
        int command;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                command = scanner.nextInt();
                if (command == 1)
                    return;
                else
                    System.out.println("[!] Введите 1, чтобы продолжить"); // сообщение, если введённая команда не 1
            } else
                System.out.println("[!] Введите 1, чтобы продолжить"); // сообщение, если введённая команда не целое число
        }
    }

    private static void fillRandomStatistics(String month, int minRandomValueOfSteps, int maxRandomValueOfSteps) {
        StepTracker.addMonth(month);
        Random r = new Random();
        for (int i = 0; i < StepTracker.months.get(month).length; i++) {
            StepTracker.months.get(month)[i] = r.nextInt(minRandomValueOfSteps, maxRandomValueOfSteps);
        }
    }

    private static void fillRandomStatistics(String month, int finishDay, int minRandomValueOfSteps, int maxRandomValueOfSteps) {
        StepTracker.addMonth(month);
        Random r = new Random();
        for (int i = 0; i <= finishDay; i++) {
            StepTracker.months.get(month)[i] = r.nextInt(minRandomValueOfSteps, maxRandomValueOfSteps);
        }
    }
}