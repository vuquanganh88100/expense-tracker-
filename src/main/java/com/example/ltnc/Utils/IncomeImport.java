package com.example.ltnc.Utils;

import com.example.ltnc.Controller.ExpenseController;
import com.example.ltnc.Controller.IncomeController;
import com.example.ltnc.Service.InComeService;

import java.sql.Timestamp;
import java.time.LocalDate;


public class IncomeImport implements FinancialImport{
    IncomeController incomeController = new IncomeController();

    private final InComeService inComeService = new InComeService();
    @Override
    public int findOrCreateCategory(String categoryName) {
        return incomeController.getCategoryIdMap().get(categoryName);
    }

    @Override
    public void saveRecord(int categoryId, String item, Long cost, String description, LocalDate date, Timestamp createdAt) {
        inComeService.add(categoryId, item, cost, description, date);
    }
}
