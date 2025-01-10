package com.example.ltnc.Utils;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.Category.CategoryEnum;
import com.example.ltnc.ExpenseController;
import com.example.ltnc.Service.CategoryService;
import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.SessionManager;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class ExpenseImport implements FinancialImport{
    ExpenseController expenseController = new ExpenseController();
    private final ExpenseService expenseService = new ExpenseService();
    @Override
    public int findOrCreateCategory(String categoryName) {

        Integer categoryId = getCategoryIdFromCache(categoryName);

        if (categoryId == null) {
            addCategoryAndUpdateCache(categoryName);
            categoryId = getCategoryIdFromCache(categoryName);
        }

        return categoryId;
    }


    private Integer getCategoryIdFromCache(String categoryName) {
        return expenseController.getCategoryIdMap().get(categoryName);
    }

    private void addCategoryAndUpdateCache(String categoryName) {
        String type = String.valueOf(CategoryEnum.EXPENSE);
        int userId = SessionManager.getInstance().getUserId();

        new CategoryService().add(categoryName, type);

        List<CategoryEntiy> categories = new CategoryDao().categoryBox(userId, "Expense");
        categories.forEach(category ->
                expenseController.getCategoryIdMap().put(category.getName(), category.getId())
        );
    }

    @Override
    public void saveRecord(int categoryId, String item, Long cost, String description, LocalDate date, Timestamp createdAt) {
        expenseService.add(categoryId, item, cost, description, date);
    }

}
