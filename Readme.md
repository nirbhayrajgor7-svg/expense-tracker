# Expense Tracker (Java)

A small command-line expense tracker. You can add expenses (category, amount, date) and view a few summaries.

## Features
- Add an expense (category, amount, date)
- List all expenses
- Show total expense
- Show total by category
- Show highest and lowest spend category
- Show expense trend (total per day)
- Saves data to a local CSV file (`expenses.csv`) so it stays after you exit

## Tech
- Java 17
- Maven
- No database (CSV file storage)

## Project structure
- `src/main/java` - application code
- `src/main/resources` - (not used right now)
- `src/test/java` - (empty for now)
- `expenses.csv` - saved expenses (created/updated when you add an expense)

## How to run (IntelliJ)
1. Open the project in IntelliJ
2. Run `App.java` (the main class)
3. Use the menu in the console

## How to run (command line)
From the project root:
```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.nirbhay.expensetracker.App"
