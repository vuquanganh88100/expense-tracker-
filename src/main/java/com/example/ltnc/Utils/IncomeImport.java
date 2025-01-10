package com.example.ltnc.Utils;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.Category.CategoryEnum;
import com.example.ltnc.IncomeController;
import com.example.ltnc.Service.CategoryService;
import com.example.ltnc.Service.InComeService;
import com.example.ltnc.Service.SessionManager;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class IncomeImport implements FinancialImport{
    IncomeController incomeController = new IncomeController();
    private final InComeService inComeService = new InComeService();
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
        return incomeController.getCategoryIdMap().get(categoryName);
    }

    private void addCategoryAndUpdateCache(String categoryName) {
        String type = String.valueOf(CategoryEnum.INCOME);
        int userId = SessionManager.getInstance().getUserId();

        new CategoryService().add(categoryName, type);

        List<CategoryEntiy> categories = new CategoryDao().categoryBox(userId, type);
        categories.forEach(category ->
                incomeController.getCategoryIdMap().put(category.getName(), category.getId())
        );
    }

    @Override
    public void saveRecord(int categoryId, String item, Long cost, String description, LocalDate date, Timestamp createdAt) {
        inComeService.add(categoryId, item, cost, description, date);
    }
}
