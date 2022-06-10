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

    static void inputSteps() {
        while (true) {
            System.out.println("Введите дату в формате год-месяц-день (например, " + LocalDate.now() + "):");
            Scanner scanner = new Scanner(System.in);
            String date = scanner.next();
            // pattern: 2022-04-01 (1 апреля 2022)
            Pattern datePattern = Pattern.compile("^[12][0-9][0-9][0-9]-[01][0-9]-[0-3][0-9]$");
            Matcher match = datePattern.matcher(date);
            if (match.matches()) { // Если дата соответствует паттерну
                while (true) {
                    System.out.println("Введите, сколько шагов прошли " + date + ":");
                    scanner = new Scanner(System.in);
                    if (scanner.hasNextInt()) {
                        int stepsPerDay = scanner.nextInt();
                        addStatisticsRecord(date, stepsPerDay);
                        System.out.println("Статистика обновлена. " + date + ": " + stepsPerDay + " \uD83D\uDC63");
                        break;
                    } else
                        System.out.println("[!] Нужно ввести целое число.");
                }
                break;
            } else
                System.out.println("[!] Некорректный формат ввода даты.");
        }
    }

    static void inputSteps(String уearMonthDay) {
        while (true) {
            System.out.println("Введите, сколько шагов прошли " + уearMonthDay + ":");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                int stepsPerDay = scanner.nextInt();
                addStatisticsRecord(уearMonthDay, stepsPerDay);
                System.out.println("Статистика обновлена. " + уearMonthDay + ": " + stepsPerDay + " \uD83D\uDC63");
                break;
            } else
                System.out.println("[!] Нужно ввести целое число.");
        }
    }

    private static void addStatisticsRecord(String yearMonthDay, int stepsPerDay) {
        // Разбиение строки с датой вида "2022-05-10" на подстроки "2022", "05", "10"
        String[] parts = yearMonthDay.split("-");
        String yearMonth = parts[0] + "-" + parts[1];   // "2022-05"
        int day = Integer.parseInt(parts[2]);           // "10"
        if (!(months.containsKey(yearMonth)))           // если в статистике ещё нет введённого месяца
            addMonth(yearMonth);                        // создать новый месяц и добавить значение шагов в соответствующий день месяца
        months.get(yearMonth)[day - 1] = stepsPerDay;   // добавить значение шагов в соответствующий день месяца
    }

    static void addMonth(String yearMonth) {
        months.put(yearMonth, new int[30]); // для скорости прототипирования длина месяца = 30 дней
    }

    static void printAllMonths() {
        System.out.println("Список всех месяцев, по которым есть статистика:");
        // Сортировка списка ключей хеш-карты (без этого ключи хещ-карты выводятся не по порядку)
        Map<String, int[]> sortedMonths = new TreeMap<>(months);
        for (String s : sortedMonths.keySet()) {
            System.out.println(s);
        }
    }

    static String selectMonth() {
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
                    System.out.println("[!] На такой месяц нет статистики.");
                } else
                    return month;
            } else
                System.out.println("[!] Некорректный формат ввода месяца.");
        }
    }

    static void printStatisticsForOneMonth(String month) {
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
        int avgStepsPerDay, records = 0;
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

    public static void printDailyGoal() {
        System.out.println("Текущая цель: " + dailyGoal + " \uD83D\uDC63/день");
    }

    public static void changeDailyGoal() {
        while (true) {
            System.out.println("Введите, сколько шагов в день вы планируете делать:");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                dailyGoal = scanner.nextInt();
                System.out.println("Установлена новая цель: " + dailyGoal + " \uD83D\uDC63/день");
                break;
            } else
                System.out.println("[!] Введите целое число.");
        }
    }

    public static String getThisMonth() {
        LocalDate yearMonthDay = LocalDate.now(); // format: 2022-05-01
        // "2022-05-10" → {"2022", "05", "10"}
        var parts = String.valueOf(yearMonthDay).split("-");
        return parts[0] + "-" + parts[1]; // "2022-05"
    }

    public static String getPreviousMonth() {
        LocalDate yearMonthDay = LocalDate.now(); // format: 2022-05-01
        LocalDate yearPreviousMonthDay = yearMonthDay.minus(Period.ofMonths(1)); // замена месяца даты на предыдуший
        // "2022-05-10" → {"2022", "05", "10"}
        String[] parts = String.valueOf(yearPreviousMonthDay).split("-");
        return parts[0] + "-" + parts[1]; // "2022-05"
    }
}