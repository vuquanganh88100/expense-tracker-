package com.example.ltnc.Utils;

import com.example.ltnc.ExpenseController;
import com.example.ltnc.Service.ExpenseService;

import java.sql.Timestamp;
import java.time.LocalDate;

public class ExpenseImport implements FinancialImport{
    ExpenseController expenseController = new ExpenseController();
    final ExpenseService expenseService = new ExpenseService();
    @Override
    public int findOrCreateCategory(String categoryName) {
        return expenseController.getCategoryIdMap().get(categoryName);
    }

    @Override
    public void saveRecord(int categoryId, String item, Long cost, String description, LocalDate date, Timestamp createdAt) {
        expenseService.add(categoryId, item, cost, description, date);
    }

}
