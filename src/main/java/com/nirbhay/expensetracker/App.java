package com.nirbhay.expensetracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class App {

    private static final List<Expense> expenses = new ArrayList<>();
    private static final Path DATA_FILE = Paths.get("expenses.csv");

    public static void main(String[] args) {
        loadExpensesFromFile();

        System.out.println("Expense Tracker starting...");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        addExpense(scanner);
                        break;
                    case "2":
                        listExpenses();
                        break;
                    case "3":
                        showTotalExpense();
                        break;
                    case "4":
                        showTotalByCategory();
                        break;
                    case "5":
                        showHighestAndLowestCategory();
                        break;
                    case "6":
                        showExpenseTrend();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown option. Try again.");
                }
            }
        }

        System.out.println("Bye.");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1) Add expense");
        System.out.println("2) List expenses");
        System.out.println("3) Show total expense");
        System.out.println("4) Show total by category");
        System.out.println("5) Show highest and lowest spend category");
        System.out.println("6) Show expense trend");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    private static void addExpense(Scanner scanner) {
        String category = readNonEmpty(scanner, "Category (e.g., Food): ").toLowerCase();
        BigDecimal amount = readAmount(scanner, "Amount (e.g., 12.50): ");
        LocalDate date = readDate(scanner, "Date (YYYY-MM-DD): ");

        Expense expense = new Expense(category, amount, date);
        expenses.add(expense);

        saveExpensesToFile();

        System.out.println("Added: " + expense);
    }

    private static void listExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        System.out.println("Expenses:");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ") " + expenses.get(i));
        }
    }

    private static void showTotalExpense() {
        if (expenses.isEmpty()) {
            System.out.println("Total expense: 0");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }

        System.out.println("Total expense: " + total);
    }

    private static void showTotalByCategory() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        Map<String, BigDecimal> totalsByCategory = buildTotalsByCategory();

        System.out.println("Total by category:");
        for (Map.Entry<String, BigDecimal> entry : totalsByCategory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void showHighestAndLowestCategory() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        Map<String, BigDecimal> totalsByCategory = buildTotalsByCategory();

        String highestCategory = null;
        BigDecimal highestTotal = null;

        String lowestCategory = null;
        BigDecimal lowestTotal = null;

        for (Map.Entry<String, BigDecimal> entry : totalsByCategory.entrySet()) {
            String category = entry.getKey();
            BigDecimal total = entry.getValue();

            if (highestTotal == null || total.compareTo(highestTotal) > 0) {
                highestTotal = total;
                highestCategory = category;
            }

            if (lowestTotal == null || total.compareTo(lowestTotal) < 0) {
                lowestTotal = total;
                lowestCategory = category;
            }
        }

        System.out.println("Highest spend category: " + highestCategory + " (" + highestTotal + ")");
        System.out.println("Lowest spend category: " + lowestCategory + " (" + lowestTotal + ")");
    }

    private static void showExpenseTrend() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        Map<LocalDate, BigDecimal> totalsByDate = buildTotalsByDate();

        System.out.println("Expense trend (total per day):");
        for (Map.Entry<LocalDate, BigDecimal> entry : totalsByDate.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static Map<String, BigDecimal> buildTotalsByCategory() {
        Map<String, BigDecimal> totalsByCategory = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            BigDecimal amount = expense.getAmount();

            BigDecimal currentTotal = totalsByCategory.getOrDefault(category, BigDecimal.ZERO);
            totalsByCategory.put(category, currentTotal.add(amount));
        }

        return totalsByCategory;
    }

    private static Map<LocalDate, BigDecimal> buildTotalsByDate() {
        // TreeMap keeps dates sorted automatically
        Map<LocalDate, BigDecimal> totalsByDate = new TreeMap<>();

        for (Expense expense : expenses) {
            LocalDate date = expense.getDate();
            BigDecimal amount = expense.getAmount();

            BigDecimal currentTotal = totalsByDate.getOrDefault(date, BigDecimal.ZERO);
            totalsByDate.put(date, currentTotal.add(amount));
        }

        return totalsByDate;
    }

    private static void loadExpensesFromFile() {
        if (!Files.exists(DATA_FILE)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(DATA_FILE)) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                // header
                if (firstLine && trimmed.equalsIgnoreCase("category,amount,date")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                String[] parts = trimmed.split(",", -1);
                if (parts.length != 3) {
                    continue; // skip bad lines
                }

                String category = parts[0].trim().toLowerCase();
                String amountRaw = parts[1].trim();
                String dateRaw = parts[2].trim();

                if (category.isEmpty() || amountRaw.isEmpty() || dateRaw.isEmpty()) {
                    continue;
                }

                BigDecimal amount;
                LocalDate date;

                try {
                    amount = new BigDecimal(amountRaw);
                    date = LocalDate.parse(dateRaw);
                } catch (Exception e) {
                    continue; // skip invalid rows
                }

                expenses.add(new Expense(category, amount, date));
            }
        } catch (IOException e) {
            System.out.println("Warning: could not read expenses.csv (" + e.getMessage() + ")");
        }
    }

    private static void saveExpensesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(
                DATA_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write("category,amount,date");
            writer.newLine();

            for (Expense expense : expenses) {
                writer.write(expense.getCategory());
                writer.write(",");
                writer.write(expense.getAmount().toPlainString());
                writer.write(",");
                writer.write(expense.getDate().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save expenses.csv (" + e.getMessage() + ")");
        }
    }

    private static String readNonEmpty(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Please enter a value.");
        }
    }

    private static BigDecimal readAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(raw);
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Amount cannot be negative.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Example: 12.50");
            }
        }
    }

    private static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use YYYY-MM-DD (example: 2026-01-17).");
            }
        }
    }
}
