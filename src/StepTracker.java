import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StepTracker {
    static HashMap<String, int[]> months = new HashMap<>(); // Хеш-карта со статистикой по всем месяцам (ключ: месяц, значение: массив со статистикой по месяцу)
    static int dailyGoal = 10_000; // значение по умолчанию ежедневной цели по шагам

    /**
     * Метод ввода статистики шагов с указанием даты
     */
    public static void inputSteps() {
        while (true) {
            System.out.println("Введите дату в формате год-месяц-день (например, " + LocalDate.now() + "):");
            Scanner scanner = new Scanner(System.in);
            String date = scanner.next();
            // Установка паттерна ввода даты: 2022-04-01
            Pattern datePattern = Pattern.compile("^[12][0-9][0-9][0-9]-[01][0-9]-[0-3][0-9]$");
            Matcher match = datePattern.matcher(date);
            if (match.matches()) { // Если дата соответствует паттерну
                while (true) {
                    System.out.println("Введите, сколько шагов прошли " + date + ":");
                    scanner = new Scanner(System.in);
                    if (scanner.hasNextInt()) {
                        int stepsPerDay = scanner.nextInt();
                        addStatisticsRecord(date, stepsPerDay); // добавить дату и введённые шаги в статистику
                        System.out.println("Статистика обновлена. " + date + ": " + stepsPerDay + " \uD83D\uDC63");
                        break;
                    } else
                        System.err.println("Нужно ввести целое число.");
                }
                break;
            } else
                System.err.println("Некорректный формат ввода даты.");
        }
    }

    /**
     * Метод ввода статистики шагов без указания даты
     */
    public static void inputSteps(String YearMonthDay) {
        String date = YearMonthDay;
        while (true) {
            System.out.println("Введите, сколько шагов прошли " + date + ":");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                int stepsPerDay = scanner.nextInt();
                addStatisticsRecord(date, stepsPerDay); // добавить дату и введённые шаги в статистику
                System.out.println("Статистика обновлена. " + date + ": " + stepsPerDay + " \uD83D\uDC63");
                break;
            } else
                System.err.println("Нужно ввести целое число.");
        }
    }

    /**
     * Метод добавления статистики по дню в статистику месяца
     */
    public static void addStatisticsRecord(String yearMonthDay, int stepsPerDay) {
        // Разбиение строки с датой вида "2022-05-10" на подстроки "2022", "05", "10"
        String[] parts = yearMonthDay.split("-");
        String yearMonth = parts[0] + "-" + parts[1];   // "2022-05"
        int day = Integer.parseInt(parts[2]);           // "10"
        if (!(months.containsKey(yearMonth)))           // если в статистике ещё нет введённого месяца
            addMonth(yearMonth);                        // создать новый месяц и добавить значение шагов в соответствующий день месяца
        months.get(yearMonth)[day - 1] = stepsPerDay;   // добавить значение шагов в соответствующий день месяца
    }

    /**
     * Метод добавления нового месяца в список месяцев
     */
    public static void addMonth(String yearMonth) {
        months.put(yearMonth, new int[30]); // для скорости прототипирования длина месяца = 30 дней
    }

    /**
     * Метод печати списка всех месяцев, по которым есть статистика
     */
    public static void printAllMonths() {
        System.out.println("\nСписок всех месяцев, по которым есть статистика:");
        // Сортировка списка ключей хеш-карты (без этого выводятся не по порядку)
        Map sortedMonths = new TreeMap<>(months);
        sortedMonths.keySet().forEach(System.out::println);
    }

    /**
     * Метод выбора месяца, по которому нужно посмотреть статистику
     */
    public static String selectMonth() {
        while (true) {
            System.out.println("Введите месяц в формате год-месяц (например, " + getThisMonth() + "):");
            Scanner scanner = new Scanner(System.in);
            String month = scanner.next();
            // Установка паттерна ввода месяца: 2022-05
            Pattern monthPattern = Pattern.compile("^[12][0-9][0-9][0-9]-[01][0-9]$");
            // Соответствует ли месяц паттерну? true/false
            Matcher matcher = monthPattern.matcher(month);
            if (matcher.matches()) {
                if (!(months.containsKey(month))) {
                    System.err.println("На такой месяц нет статистики.");
                } else
                    return month;
            } else
                System.err.println("Некорректный формат ввода месяца.");
        }
    }

    /**
     * Метод печати статистики по одному месяцу
     */
    public static void printOneMonth(String month) {
        System.out.println("СТАТИСТИКА ЗА " + month);

        // Шагов сделано
        System.out.println("Шагов сделано:");
        // Всего шагов за месяц
        int totalSteps = 0;
        for (int i = 0; i < months.get(month).length; i++) {
            totalSteps += months.get(month)[i];
        }
        System.out.println(totalSteps + " \uD83D\uDC63 всего");

        // Среднее количество шагов в день:
        int avgStepsPerDay = 0, records = 0;
        for (int i = 0; i < months.get(month).length; i++) {
            if (months.get(month)[i] != 0)
                records += 1;
        }
        avgStepsPerDay = totalSteps / records;
        System.out.println(avgStepsPerDay + " \uD83D\uDC63 в среднем за день");

        // Максимальное пройденное число шагов в день:
        int maxStepsPerDay = 0, maxStepsPerDayDate = 0;
        for (int i = 0; i < months.get(month).length; i++) {
            if (maxStepsPerDay < months.get(month)[i]) {
                maxStepsPerDay = months.get(month)[i];
                maxStepsPerDayDate = i + 1;
            }
        }
        System.out.println(maxStepsPerDay + " \uD83D\uDC63 — рекорд по шагам в день (установлен " + month + "-" + maxStepsPerDayDate + ")");

        // Лучшая серия: максимальное количество подряд идущих дней, в течение которых количество шагов за день было выше целевого.
        int seriesDurationMax = 1, seriesDurationCur = 1;
        for (int i = 1; i < months.get(month).length; i++) {
            if (months.get(month)[i] > dailyGoal) {
                if (months.get(month)[i - 1] > dailyGoal)
                    seriesDurationCur += 1;
            } else {
                if (seriesDurationCur > seriesDurationMax)
                    seriesDurationMax = seriesDurationCur;
                seriesDurationCur = 1;
            }
        }
        System.out.println(seriesDurationMax + " дн. — максимальное число дней подряд, когда цель по шагам (" + dailyGoal + " \uD83D\uDC63/день) достигалась ежедневно");

        // Пройдено километров:
        double distance = totalSteps * 0.075 / 1000; // 1 шаг = 0.75 м = 0.075 км
        System.out.println((int) distance + " км пройдено");

        // Сожжено килокалорий:
        double kcal = totalSteps * 0.05; // 1 шаг = 50 кал = 0.05 ккал
        System.out.println((int) kcal + " ккал сожжено во время ходьбы");

        System.out.println("Сколько шагов сделано по дням:");
        for (int i = 0; i < months.get(month).length; i++) {
            if (!(months.get(month)[i] == 0))  // если за день нет записи, сколько сделано шагов
                System.out.println(month + "-" + (i + 1) + ": " + months.get(month)[i] + " \uD83D\uDC63");
        }
    }

    /**
     * Метод печати ежедневной цели
     */
    public static void printDailyGoal() {
        System.out.println("Текущая цель: " + dailyGoal + " \uD83D\uDC63/день");
    }

    /**
     * Метод изменения ежедневной цели
     */
    public static void changeDailyGoal() {
        while (true) {
            System.out.println("Введите, сколько шагов в день вы планируете делать:");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                dailyGoal = scanner.nextInt();
                System.out.println("Установлена новая цель: " + dailyGoal + " \uD83D\uDC63/день");
                break;
            } else
                System.err.println("Введите целое число.");
        }
    }

    /**
     * Метод получения текущего месяца вида 2022-05
     */
    public static String getThisMonth() {
        LocalDate yearMonthDay = LocalDate.now(); // получение сегодняшней даты в формате 2022-05-01
        // Разбиение строки с датой вида "2022-05-10" на подстроки "2022", "05", "10"
        String[] parts = String.valueOf(yearMonthDay).split("-");
        String yearMonth = parts[0] + "-" + parts[1]; // "2022-05"
        return yearMonth;
    }

    /**
     * Метод получения предыдущего месяца вида 2022-04
     */
    public static String getPreviousMonth() {
        LocalDate yearMonthDay = LocalDate.now(); // получение сегодняшней даты в формате 2022-05-01
        LocalDate yearPreviousMonthDay = yearMonthDay.minus(Period.ofMonths(1)); // замена месяца даты на предыдуший
        // Разбиение строки с датой вида "2022-04-10" на подстроки "2022", "04", "10"
        String[] parts = String.valueOf(yearPreviousMonthDay).split("-");
        String yearPreviousMonth = parts[0] + "-" + parts[1]; // "2022-04"
        return yearPreviousMonth;
    }
}