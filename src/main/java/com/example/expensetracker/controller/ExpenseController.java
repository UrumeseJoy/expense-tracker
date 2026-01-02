package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.ExpenseService;
import com.example.expensetracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(
            ExpenseService expenseService,
            UserService userService
    ) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    private User getCurrentUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        return userService
                .findByUsername(auth.getName())
                .orElseThrow();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        User user = getCurrentUser();
        List<Expense> expenses =
                expenseService.getExpensesForUser(user);

        model.addAttribute("expenses", expenses);
        model.addAttribute("expense", new Expense());

        return "dashboard";
    }

    @PostMapping("/expenses/add")
    public String addExpense(
            @ModelAttribute Expense expense
    ) {
        expenseService.saveExpense(expense, getCurrentUser());
        return "redirect:/dashboard";
    }

    @PostMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id, getCurrentUser());
        return "redirect:/dashboard";
    }
}
