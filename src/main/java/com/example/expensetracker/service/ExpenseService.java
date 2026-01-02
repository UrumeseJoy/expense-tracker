package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense saveExpense(Expense expense, User user) {
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesForUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public void deleteExpense(Long expenseId, User user) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        expenseRepository.delete(expense);
    }

    public List<Expense> getExpensesForMonth(User user, int year, int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return expenseRepository.findByUserAndDateBetween(user, start, end);
    }
}
