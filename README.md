# Expense Tracker (CLI – Java)

This is a simple command-line expense tracker built using Java.
It allows users to record daily expenses and view basic spending insights.


## What this app does

You can:

* Add an expense (category, amount, date)
* List all expenses
* View total spending
* View total spending by category
* View spending trend (total per day)
* See highest and lowest spend categories

All data is saved in a local CSV file so expenses are not lost when the app is closed.

## Tech used

* Java 17
* Maven
* Command Line (CLI)
* CSV file for storage (no database)


## Project structure

```
expense-tracker
├── src
│   └── main
│       └── java
│           └── com.nirbhay.expensetracker
│               ├── App.java        # Main application logic
│               └── Expense.java    # Expense model
├── expenses.csv                    # Stored expense data
├── pom.xml                         # Maven config
└── README.md
```


## How data is stored

* Expenses are stored in `expenses.csv` in the project root
* Format:

  ```
  category,amount,date
  food,20.50,2025-11-17
  gas,50,2025-11-16
  ```
* Data is loaded automatically when the app starts
* Data is saved automatically when a new expense is added


## How to run the app

### Option 1: Run from IntelliJ (recommended)

1. Open the project in IntelliJ
2. Open `App.java`
3. Click **Run**
4. Follow the on-screen menu


### Option 2: Run from command line

Make sure Java and Maven are installed.

From the project root:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.nirbhay.expensetracker.App"


## Example menu

1) Add expense
2) List expenses
3) Show total expense
4) Show total by category
5) Show highest and lowest spend category
6) Show expense trend
0) Exit

## Design approach

* Kept the app simple and readable
* Used plain Java collections (`List`, `Map`)
* Avoided frameworks and databases
* Focused on correctness and clarity over overengineering


## Notes

* Categories are stored in lowercase to avoid duplicates like `Gas` and `gas`
* Input validation is included for amount and date
* This project is intended as a simple demonstration, not a production system


## Author

**Nirbhay Rajgor**

