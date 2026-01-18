package com.nirbhay.expensetracker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Expense {
    private final String category;
    private final BigDecimal amount;
    private final LocalDate date;

    public Expense(String category, BigDecimal amount, LocalDate date) {
        this.category = Objects.requireNonNull(category, "category cannot be null").trim();
        this.amount = Objects.requireNonNull(amount, "amount cannot be null");
        this.date = Objects.requireNonNull(date, "date cannot be null");

        if (this.category.isEmpty()) {
            throw new IllegalArgumentException("category cannot be empty");
        }
        if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
